package vn.iotstar.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.services.CategoryService;
import vn.iotstar.entity.Category;
import vn.iotstar.models.CategoryModel;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@RequestMapping("")
	public String all(Model model) {
		List<Category> list = categoryService.findAll();
		model.addAttribute("list", list);

		return "admin/category/list1";
	}

	@GetMapping("/add")
	public String add(Model model) {
		CategoryModel category = new CategoryModel();
		category.setIsEdit(false);
		model.addAttribute("category", category);

		return "admin/category/add";

	}

	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/category/add");
		}

		Category entity = new Category();
		// Copy từ Model sang Entity
		BeanUtils.copyProperties(cateModel, entity);
		// Gọi hàm save trong service
		categoryService.save(entity);
		// Đưa thông báo về cho biến message
		String message = "";
		if (cateModel.getIsEdit() == true) {
			message = "Category is Edited!";
		} else {
			message = "Category is saved!";
		}

		model.addAttribute("message", message);
		// Redirect về URL controller
		return new ModelAndView("forward:/admin/categories", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = categoryService.findById(categoryId);

		if (optCategory.isPresent()) {
			Category entity = optCategory.get();
			CategoryModel cateModel = new CategoryModel();
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			model.addAttribute("category", cateModel);
			return new ModelAndView("admin/category/add", model);
		} else {
			model.addAttribute("message", "Category is not existed!!!!");
			return new ModelAndView("forward:/admin/categories", model);
		}
	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category is deleted!!!!");
		return new ModelAndView("forward:/admin/categories", model);
	}

	@RequestMapping("/searchpaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int count = (int) categoryService.count();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<Category> resultPage = null;
		if (StringUtils.hasText(name)) {
			resultPage = categoryService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			resultPage = categoryService.findAll(pageable);
		}
		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > count) {
				if (end == totalPages)
					start = end - count;
				else if (start == 1)
					end = start + count;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("categoryPage", resultPage);
		return "admin/category/list1";
	}

}
