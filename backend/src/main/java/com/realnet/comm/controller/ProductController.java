package com.realnet.comm.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.realnet.comm.entity.ProductEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.google.common.net.MediaType;
import com.realnet.fnd.response.ProductResponse;
import com.realnet.fnd.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/product" })
public class ProductController {
	@Autowired
	private ProductService productService;
	// get all data
	@ApiOperation(value = "List of Product", response = ProductResponse.class)
	@GetMapping(path = "/getproduct")
	public ProductResponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		ProductResponse resp = new ProductResponse();
		Pageable paging = PageRequest.of(page, size);
		Page<ProductEntity> result =  productService.getlist(paging);
		resp.setPageStats(result, true);
		resp.setProductEntity(result.getContent());
		return resp;
	}
	// @RequestParam("file") MultipartFile file ,
		// add data
		@ApiOperation(value = "add a product", response = ProductEntity.class)
		@PostMapping(path = "/getproduct")
		public ResponseEntity<?> savedata(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
				@RequestBody List<ProductEntity> productEntity) {
			// String filename=file.getOriginalFilename();
			// System.err.println(filename);
			// sale.setUploadprofile(filename);
			//
			System.out.println(productEntity);
			List<ProductEntity> saveproduct = productService.saveproduct(productEntity);
			if (saveproduct == null) {
				throw new ResourceNotFoundException("product not saved");
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(saveproduct);
		}
		
		// get data by id
		@ApiOperation(value = "product", response = ProductEntity.class)
		@GetMapping("/getproduct/{id}" )
		public ResponseEntity<ProductEntity> getbyid(@PathVariable("id") Integer id) {
			 ProductEntity getid = productService.getid(id);
			if (getid == null) {
				throw new ResourceNotFoundException("id not found with id " + id);
			}
			return ResponseEntity.ok().body(getid);
		}
		// UPDATE
		@ApiOperation(value = "update a product", response = ProductEntity.class)
		@PutMapping("/getproduct/{id}")
		public ResponseEntity<ProductEntity> updateProduct(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
				@PathVariable(value = "id") Integer id, @Valid @RequestBody ProductEntity productEntity) {
			ProductEntity updatedsale = productService.updateById(id, productEntity);
			if (updatedsale == null || id != updatedsale.getProductid()) {
				throw new ResourceNotFoundException("product not found with id " + id);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
		}
		
		// DELETE
		@DeleteMapping("/getproduct/{id}")
		public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable(value = "id") Integer id) {
			boolean delete = productService.deleteById(id);
			Map<String, Boolean> response = new HashMap<>();
			if (delete) {
				response.put("delete", Boolean.TRUE);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
}
