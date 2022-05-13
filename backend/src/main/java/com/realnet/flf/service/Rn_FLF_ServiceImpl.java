package com.realnet.flf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.flf.entity.Rn_FLF_Header;
import com.realnet.flf.entity.Rn_FLF_Line;
import com.realnet.flf.repository.Rn_FLF_Header_Repository;
import com.realnet.flf.repository.Rn_FLF_Line_Repository;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_FLF_ServiceImpl implements Rn_FLF_Service {

	@Autowired(required=false)
	private UserService userService;

	@Autowired
	private Rn_FLF_Header_Repository rn_flf_headerRepository;

	@Autowired
	private Rn_FLF_Line_Repository rn_flf_lineRepository;;

	@Override
	public List<Rn_FLF_Header> getAll() {
		return rn_flf_headerRepository.findAll();
	}

	@Override
	public Page<Rn_FLF_Header> getAll(Pageable page) {
		return rn_flf_headerRepository.findAll(page);
	}

	@Override
	public Rn_FLF_Header getById(int id) {
		Rn_FLF_Header rn_flf_header = rn_flf_headerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		return rn_flf_header;
	}

	@Override
	public Rn_FLF_Header save(Rn_FLF_Header rn_flf_header) {
		User loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
		long accId = loggedInUser.getSys_account().getId();
		rn_flf_header.setCreatedBy(userId);
		rn_flf_header.setUpdatedBy(userId);
		//rn_flf_header.setAccountId(accId);
		Rn_FLF_Header savedHeader = rn_flf_headerRepository.save(rn_flf_header);
		return savedHeader;
	}

	@Override
	public Rn_FLF_Header updateById(int id, Rn_FLF_Header headerRequest) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();
		Long accId = loggedInUser.getSys_account().getId();

		Rn_FLF_Header oldHeader = rn_flf_headerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		
		oldHeader.setTechStack(headerRequest.getTechStack());
		oldHeader.setObjectType(headerRequest.getObjectType());
		oldHeader.setSubObjectType(headerRequest.getSubObjectType());
		oldHeader.setFieldtype(headerRequest.getFieldtype());
		oldHeader.setActive(headerRequest.isActive());
		oldHeader.setBuild(headerRequest.isBuild());
		// line part
		oldHeader.setRn_flf_lines(headerRequest.getRn_flf_lines());
		oldHeader.setUpdatedBy(userId);
		//oldHeader.setAccountId(accId);
		final Rn_FLF_Header updatedHeader = rn_flf_headerRepository.save(oldHeader);
		return updatedHeader;
	}

	@Override
	public boolean deleteById(int id) {
		if (!rn_flf_headerRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_FLF_Header actionBuilderHeader = rn_flf_headerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		rn_flf_headerRepository.delete(actionBuilderHeader);
		return true;
	}

	// ------- LINES ---------- //

//	@Override
//	public List<Rn_FLF_Header> getAll() {
//		return rn_flf_headerRepository.findAll();
//	}
//
//	@Override
//	public Page<Rn_FLF_Header> getAll(Pageable page) {
//		return rn_flf_headerRepository.findAll(page);
//	}

	// mod needed
	@Override
	public Page<Rn_FLF_Line> getLinesByHeaderId(int headerId, Pageable p) {
		return rn_flf_lineRepository.findByHeaderId(headerId, p);
	}

	@Override
	public Rn_FLF_Line getLineById(int id) {
		Rn_FLF_Line rn_flf_line = rn_flf_lineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		return rn_flf_line;
	}

	@Override
	public Rn_FLF_Line saveLine(int headerId, Rn_FLF_Line rb_flf_line) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();
		//Long accId = loggedInUser.getSys_account().getId();

		Rn_FLF_Header rn_flf_header = this.getById(headerId);
		rb_flf_line.setRn_flf_header(rn_flf_header);
		//rb_flf_line.setAccountId(accId);
		rb_flf_line.setCreatedBy(userId);
		rb_flf_line.setUpdatedBy(userId);
		Rn_FLF_Line savedLine = rn_flf_lineRepository.save(rb_flf_line);
		return savedLine;
	}

	@Override
	public Rn_FLF_Line updateLineById(int id, Rn_FLF_Line rn_flf_LineRequest) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();

		Rn_FLF_Line oldLine = rn_flf_lineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		
		oldLine.setTechStack(rn_flf_LineRequest.getTechStack());
		oldLine.setObjectType(rn_flf_LineRequest.getObjectType());
		oldLine.setSubObjectType(rn_flf_LineRequest.getSubObjectType());
		oldLine.setJava(rn_flf_LineRequest.getJava());
		oldLine.setJsp(rn_flf_LineRequest.getJsp());
		oldLine.setJavascript(rn_flf_LineRequest.getJavascript());
		oldLine.setXml(rn_flf_LineRequest.getXml());
		oldLine.setFieldtype(rn_flf_LineRequest.getFieldtype());
		oldLine.setUpdatedBy(userId);
		
		final Rn_FLF_Line updatedLine = rn_flf_lineRepository.save(oldLine);
		return updatedLine;
	}

	@Override
	public boolean deleteLineById(int id) {
		if (!rn_flf_lineRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_FLF_Line actionBuilderLine = rn_flf_lineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		rn_flf_lineRepository.delete(actionBuilderLine);
		return true;
	}

}
