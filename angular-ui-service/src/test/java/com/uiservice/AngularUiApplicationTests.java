package com.uiservice;

import com.productservice.controller.ProductController;
import com.productservice.service.ProductService;
import com.shopcommon.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = AngularUiApplication.class)
public class AngularUiApplicationTests {

//	@Mock
//	private ProductController productController;
//
//	@Autowired
//	RabbitTemplate rabbitTemplate;
//
////	@Test
////	public void contextLoads() {
////
////		when(productController.getProducts()).thenReturn(sendProductsToQueue()).;
////
////	}
//
//	private List<Product> sendProductsToQueue(){
//		List<Product> products = new ArrayList<>();
//		Product p1 = new Product();
//		p1.setPrice(10);
//		p1.setStock(100);
//		p1.setName("product1");
//		p1.setId(1L);
//
//
//		Product p2 = new Product();
//		p2.setPrice(20);
//		p2.setStock(200);
//		p2.setName("product2");
//		p2.setId(2L);
//
//
//		Product p3 = new Product();
//		p3.setPrice(30);
//		p3.setStock(300);
//		p3.setName("product3");
//		p3.setId(3L);
//
//		products.add(p1);
//		products.add(p2);
//		products.add(p3);
//
//		rabbitTemplate.convertAndSend("product-queue", products);
//
//		return products;
//	}

	@Test
	public void load(){

	}
}
