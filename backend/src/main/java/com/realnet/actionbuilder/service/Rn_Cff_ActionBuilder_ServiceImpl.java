package com.realnet.actionbuilder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Lines;
import com.realnet.actionbuilder.repository.Rn_CFF_ActionBuilder_Header_Repository;
import com.realnet.actionbuilder.repository.Rn_CFF_ActionBuilder_Lines_Repository;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_Cff_ActionBuilder_ServiceImpl implements Rn_Cff_ActionBuilder_Service {

	@Autowired(required=false)
	private UserService userService;

	@Autowired
	private Rn_CFF_ActionBuilder_Header_Repository rn_cff_ActionBuilderHeaderRepository;

	@Autowired
	private Rn_CFF_ActionBuilder_Lines_Repository rn_cff_ActionBuilderLineRepository;;

	@Override
	public List<Rn_cff_ActionBuilder_Header> getAll() {
		return rn_cff_ActionBuilderHeaderRepository.findAll();
	}

	@Override
	public Page<Rn_cff_ActionBuilder_Header> getAll(Pageable page) {
		return rn_cff_ActionBuilderHeaderRepository.findAll(page);
	}

	@Override
	public Rn_cff_ActionBuilder_Header getById(int id) {
		Rn_cff_ActionBuilder_Header actionBuilderHeader = rn_cff_ActionBuilderHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		return actionBuilderHeader;
	}

	@Override
	public Rn_cff_ActionBuilder_Header save(Rn_cff_ActionBuilder_Header actionBuilderHeader) {
		User loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
		long accId = loggedInUser.getSys_account().getId();
		actionBuilderHeader.setCreatedBy(userId);
		actionBuilderHeader.setUpdatedBy(userId);
		actionBuilderHeader.setAccountId(accId);
		Rn_cff_ActionBuilder_Header savedHeader = rn_cff_ActionBuilderHeaderRepository.save(actionBuilderHeader);
		return savedHeader;
	}

	@Override
	public Rn_cff_ActionBuilder_Header updateById(int id, Rn_cff_ActionBuilder_Header headerRequest) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();

		Rn_cff_ActionBuilder_Header oldHeader = rn_cff_ActionBuilderHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Menu not found :: " + id));
		oldHeader.setUpdatedBy(userId);
		oldHeader.setTechnologyStack(headerRequest.getTechnologyStack());
		oldHeader.setControllerName(headerRequest.getControllerName());
		oldHeader.setMethodName(headerRequest.getMethodName());
		oldHeader.setActionName(headerRequest.getActionName());
		oldHeader.setFileLocation(headerRequest.getFileLocation());
		// line part
		oldHeader.setActionBuilderLines(headerRequest.getActionBuilderLines());
		final Rn_cff_ActionBuilder_Header updatedHeader = rn_cff_ActionBuilderHeaderRepository.save(oldHeader);
		return updatedHeader;
	}

	@Override
	public boolean deleteById(int id) {
		if (!rn_cff_ActionBuilderHeaderRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_cff_ActionBuilder_Header actionBuilderHeader = rn_cff_ActionBuilderHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		rn_cff_ActionBuilderHeaderRepository.delete(actionBuilderHeader);
		return true;
	}

	// ------- LINES ---------- //

//	@Override
//	public List<Rn_cff_ActionBuilder_Header> getAll() {
//		return rn_cff_ActionBuilderHeaderRepository.findAll();
//	}
//
//	@Override
//	public Page<Rn_cff_ActionBuilder_Header> getAll(Pageable page) {
//		return rn_cff_ActionBuilderHeaderRepository.findAll(page);
//	}

	// mod needed
	@Override
	public Page<Rn_cff_ActionBuilder_Lines> getLinesByHeaderId(int headerId, Pageable p) {
		return rn_cff_ActionBuilderLineRepository.findByHeaderId(headerId, p);
	}

	@Override
	public Rn_cff_ActionBuilder_Lines getLineById(int id) {
		Rn_cff_ActionBuilder_Lines actionBuilderLine = rn_cff_ActionBuilderLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		return actionBuilderLine;
	}

	@Override
	public Rn_cff_ActionBuilder_Lines saveLine(int headerId, Rn_cff_ActionBuilder_Lines actionBuilderLine) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();
		Long accId = loggedInUser.getSys_account().getId();

		Rn_cff_ActionBuilder_Header header = this.getById(headerId);
		actionBuilderLine.setRn_cff_actionBuilderHeader(header);
		actionBuilderLine.setAccountId(accId);
		actionBuilderLine.setCreatedBy(userId);
		actionBuilderLine.setUpdatedBy(userId);
		// or
		// header.getActionBuilderLines().add(actionBuilderLine);

		Rn_cff_ActionBuilder_Lines savedLine = rn_cff_ActionBuilderLineRepository.save(actionBuilderLine);
		return savedLine;
	}

	@Override
	public Rn_cff_ActionBuilder_Lines updateLineById(int id, Rn_cff_ActionBuilder_Lines actionBuilderLineRequest) {
		User loggedInUser = userService.getLoggedInUser();
		Long userId = loggedInUser.getUserId();

		Rn_cff_ActionBuilder_Lines oldLine = rn_cff_ActionBuilderLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		oldLine.setUpdatedBy(userId);
		oldLine.setActionType1(actionBuilderLineRequest.getActionType1());
		oldLine.setActionType2(actionBuilderLineRequest.getActionType2());
		oldLine.setDataType(actionBuilderLineRequest.getDataType());
		oldLine.setVariableName(actionBuilderLineRequest.getVariableName());
		oldLine.setAssignment(actionBuilderLineRequest.getAssignment());
		oldLine.setMessage(actionBuilderLineRequest.getMessage());
		oldLine.setConditions(actionBuilderLineRequest.getConditions());
		oldLine.setForward(actionBuilderLineRequest.getForward());
		oldLine.setEquation(actionBuilderLineRequest.getEquation());
		oldLine.setSeq(actionBuilderLineRequest.getSeq());
		oldLine.setAction(actionBuilderLineRequest.getAction());
		oldLine.setGroupId(actionBuilderLineRequest.getGroupId());
		final Rn_cff_ActionBuilder_Lines updatedLine = rn_cff_ActionBuilderLineRepository.save(oldLine);
		return updatedLine;
	}

	@Override
	public boolean deleteLineById(int id) {
		if (!rn_cff_ActionBuilderLineRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_cff_ActionBuilder_Lines actionBuilderLine = rn_cff_ActionBuilderLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		rn_cff_ActionBuilderLineRepository.delete(actionBuilderLine);
		return true;
	}

	// ---- business logic
	@Override
	public List<Rn_cff_ActionBuilder_Lines> getLinesByHeaderIdAndOrderBySeq(int headerId) {
		return rn_cff_ActionBuilderLineRepository.findByHeaderIdAndOrderBySeq(headerId);
	}

	@Override
	public Rn_cff_ActionBuilder_Lines getLinesByHeaderIdAndSeq(int headerId, int seq) {
		return rn_cff_ActionBuilderLineRepository.findByHeaderIdAndSeq(headerId, seq);
	}

}
