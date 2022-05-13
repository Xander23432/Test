//package com.realnet.formdrag.services.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.realnet.formdrag.entity.Theme;
//import com.realnet.formdrag.repository.ThemeRepository;
//import com.realnet.formdrag.services.ThemeService;
//
//@Service
//public class ThemeServiceImpl implements ThemeService {
//	
//	@Autowired
//	private ThemeRepository themeRepository;
//
//	@Override
//	public Theme createTheme(Theme theme) {
//		return this.themeRepository.save(theme);
//	}
//
//	@Override
//	public Theme updateTheme(Theme theme) {
//		return this.themeRepository.save(theme);
//	}
//
//	@Override
//	public Theme getTheme(Long tId) {
//		return this.themeRepository.findById(tId).get();
//	}
//
//	@Override
//	public List<Theme> getAllThemes() {
//		return this.themeRepository.findAll();
//	}
//
//	@Override
//	public void deleteTheme(Long tId) {
//		this.themeRepository.deleteById(tId);
//	}
//
//}
