package com.company.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.services.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

class CategoryRestControllerTest {

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@InjectMocks
	CategoryRestController categoryRestController;

	@Mock
	private ICategoryService service;

	List<Category> list = new ArrayList<Category>();

	@BeforeEach
	public void init() {
		chargueCategory();
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(categoryRestController).build();

	}

	@Test
	public void getCategroryTest() {

		when(service.search()).thenReturn(new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));

		ResponseEntity<CategoryResponseRest> response = categoryRestController.searchCategories();

		assertThat(response.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void getCategroryTest2() throws Exception {

		CategoryResponseRest categoryResponse = new CategoryResponseRest();
		categoryResponse.getCategoryResponse().setCategory(list);
		categoryResponse.setMetadata("Respuesta ok", "00", "Categoria encontrada");

		when(service.search()).thenReturn(new ResponseEntity<CategoryResponseRest>(categoryResponse, HttpStatus.OK));

		this.mockMvc
				.perform(get("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.categoryResponse").exists())
				.andExpect(jsonPath("$.categoryResponse.category[0].name").value("Abarrotes"))
				.andExpect(status().isOk());

	}

	@Test
	public void getCategroryTestByIdTest() throws Exception {

		CategoryResponseRest categoryResponse = new CategoryResponseRest();
		categoryResponse.getCategoryResponse().setCategory(list);
		categoryResponse.setMetadata("Respuesta ok", "00", "Categoria encontrada");

		when(service.searchById(list.get(0).getId()))
				.thenReturn(new ResponseEntity<CategoryResponseRest>(categoryResponse, HttpStatus.OK));

		this.mockMvc
				.perform(get("/api/v1/categories/1").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.categoryResponse").exists())
				.andExpect(jsonPath("$.categoryResponse.category[0].name").value("Abarrotes"))
				.andExpect(status().isOk());

	}

	@Test
	public void saveTest() throws Exception {

		CategoryResponseRest categoryResponse = new CategoryResponseRest();

		List<Category> listSave = new ArrayList<Category>();

		Category catSave = new Category(Long.valueOf(1), "Dulces", "Distintos tipos de Dulces");

		listSave.add(catSave);

		categoryResponse.getCategoryResponse().setCategory(listSave);

		categoryResponse.setMetadata("Respuesta ok", "00", "Categoria encontrada");

		when(service.save(catSave))
				.thenReturn(new ResponseEntity<CategoryResponseRest>(categoryResponse, HttpStatus.OK));

		this.mockMvc
				.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(catSave)).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.categoryResponse").exists())
				.andExpect(jsonPath("$.categoryResponse.category[0].name").value("Dulces")).andExpect(status().isOk());

	}

	@Test
	public void updateTest() throws Exception {

		CategoryResponseRest categoryResponse = new CategoryResponseRest();

		List<Category> listUpdate = new ArrayList<Category>();

		Category catUpdate = new Category(Long.valueOf(1), "Dulces Actualizados", "Distintos tipos de Dulces");

		listUpdate.add(catUpdate);

		categoryResponse.getCategoryResponse().setCategory(listUpdate);

		categoryResponse.setMetadata("Respuesta ok", "00", "Categoria encontrada");

		when(service.update(catUpdate, Long.valueOf("1")))
				.thenReturn(new ResponseEntity<CategoryResponseRest>(categoryResponse, HttpStatus.OK));

		this.mockMvc
				.perform(put("/api/v1/categories/1" + "").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(catUpdate)).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.categoryResponse").exists())
				.andExpect(jsonPath("$.categoryResponse.category[0].name").value("Dulces Actualizados"))
				.andExpect(status().isOk());

	}

	public void chargueCategory() {
		Category cat1 = new Category(Long.valueOf(1), "Abarrotes", "Distintos tipos de abarrotes");
		Category cat2 = new Category(Long.valueOf(1), "Lacteos", "Variedad de lacteos");
		Category cat3 = new Category(Long.valueOf(1), "Solomillo", "Cerdo ibérico");

		list.add(cat1);
		list.add(cat2);
		list.add(cat3);
	}

	@Test
	public void deleteTest() throws Exception {

		CategoryResponseRest categoryResponse = new CategoryResponseRest();

		categoryResponse.setMetadata("Respuesta ok", "00", "Respuesta exitosa");

		when(service.deleteById(Long.valueOf("1")))
				.thenReturn(new ResponseEntity<CategoryResponseRest>(categoryResponse, HttpStatus.OK));

		this.mockMvc
				.perform(delete("/api/v1/categories/1" + "")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				        .andExpect(status().isOk());

	}

}
