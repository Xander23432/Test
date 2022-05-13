package com.realnet.ncso.service.impl1;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.Billing;
import com.realnet.ncso.entity1.BillingAttachment;
import com.realnet.ncso.entity1.BillingItem;
import com.realnet.ncso.entity1.BillingSource;
import com.realnet.ncso.entity1.CreditNote;
import com.realnet.ncso.entity1.CreditNoteCompositeKey;
import com.realnet.ncso.entity1.MixOfBilling;
import com.realnet.ncso.repository1.BillingAttachmentRepo;
import com.realnet.ncso.repository1.BillingItemDetailRepository;
import com.realnet.ncso.repository1.BillingItemRepository;
import com.realnet.ncso.repository1.BillingRepository;
import com.realnet.ncso.repository1.BillingSourceRepository;
import com.realnet.ncso.repository1.CreditNoteRepository;

@Service
public class BillingService {
	private BillingRepository billingRepository;
	private CreditNoteRepository creditNoteRepository;
	private BillingAttachmentRepo billingAttachmentRepo;
	private BillingItemRepository billingItemRepository;
	private BillingItemDetailRepository billingItemDetailRepository;
	private BillingSourceRepository billingSourceRepository;
	@Autowired
	public BillingService(BillingRepository billingRepository,
			 CreditNoteRepository creditNoteRepository,
			 BillingAttachmentRepo billingAttachmentRepo,
			 BillingItemRepository billingItemRepository,
			 BillingItemDetailRepository billingItemDetailRepository,
			 BillingSourceRepository billingSourceRepository) {
		this.billingRepository = billingRepository;
		this.creditNoteRepository = creditNoteRepository;
		this.billingAttachmentRepo=billingAttachmentRepo;
		this.billingItemRepository=billingItemRepository;
		this.billingItemDetailRepository=billingItemDetailRepository;
		this.billingSourceRepository=billingSourceRepository;
	}
	public Optional<Billing> getOne(Long id) {
		return billingRepository.findById(id);
	}
	public CreditNote getOneCreditNote(Long disputeId) {
		return creditNoteRepository.findByDisputeId(disputeId).orElse(null);
	}
	
	public Billing updateBilling(Billing billing) {
		return billingRepository.save(billing);
	}
	public CreditNote updateCreditNote(CreditNote creditNote) {
		return creditNoteRepository.save(creditNote);
	}
	
	public Billing addBilling(Billing billing) {
		return billingRepository.save(billing);
	}
	public CreditNote addCreditNote(CreditNote creditNote) {
		return creditNoteRepository.save(creditNote);
	}
	public Optional<Billing> findByInvoiceNo(String invoiceNo) {
		// TODO Auto-generated method stub
		return billingRepository.findByInvoiceNo(invoiceNo);
	}
	public void deleteBilling(Long id) {
		 billingRepository.deleteById(id);
	}
	public void deleteCreditNote(CreditNoteCompositeKey creditNoteCompositeKey) {
		creditNoteRepository.deleteById(creditNoteCompositeKey);
	}
	public List<Billing> getAllBilling(Pageable page) {
		return billingRepository.findAll(page).getContent();
	}
	public List<CreditNote> getAllCreditNote(Pageable paging) {
		// TODO Auto-generated method stub
		return creditNoteRepository.findAll(paging).getContent();
	}
	public Optional<CreditNote> findByBillingId(Long billingId) {
		// TODO Auto-generated method stub
		return creditNoteRepository.findByBillingId(billingId);
	}
	public MixOfBilling findDisputeCustom(Long disputeId) {
		CreditNote c = creditNoteRepository.findByDisputeId(disputeId).orElse(null);
		Billing bb = null;
		List<BillingAttachment> a = null;
		List<BillingItem> i = null;
		List<Object> findAllBs=null;
		List<Object> findAllMarine = null;
		List<BillingSource> findAllCustom = null;
		List<Object> sorders_gkey = null;
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		if(c!=null) {
			
			 bb= billingRepository.findById(c.getBillingId()).orElse(null);
			if(bb!=null) {
				
				a = billingAttachmentRepo.findByBillingId(bb.getBillingId());
				i = billingItemRepository.findByBillingId(bb.getBillingId());
				if(bb.getInvoiceType().equals("SERVICE_ORDER")) {
					 sorders_gkey = billingItemDetailRepository.findAllCustom(bb.getBillingId());
				}else {
					findAllBs = billingSourceRepository.findAllBs(bb.getBillingId());
					for(Object s:findAllBs) {
						Object[] se = (Object[]) s;
						String str= (String) se[0];
						if(str.equals("MARINE")) {
							BigDecimal be = (BigDecimal) se[1];
							 findAllMarine = billingSourceRepository.findAllMarine(be);
						}
					}
				}
				findAllCustom = billingSourceRepository.findAllCustom(bb.getBillingId());
				bb.setInvoiceDateFormated(bb.getInvoiceDate()!=null?simple.format(bb.getInvoiceDate()):null);
				bb.setConfirmDateFormated(bb.getConfirmDate()!=null?simple.format(bb.getConfirmDate()):null);
				bb.setPostDateFormated(bb.getPostDate()!=null?simple.format(bb.getPostDate()):null);
				bb.setAtaFormated(bb.getAta()!=null?simple.format(bb.getAta()):null);
				bb.setAtdFormated(bb.getAtd()!=null?simple.format(bb.getAtd()):null);
				bb.setCreatedOnFormated(bb.getCreatedOn()!=null?simple.format(bb.getCreatedOn()):null);
				bb.setUpdatedOnFormated(bb.getUpdatedOn()!=null?simple.format(bb.getUpdatedOn()):null);
			
			}
		}
		MixOfBilling mix = new MixOfBilling();
		mix.setBilling(bb);
		mix.setCreditNote(c);
		mix.setBillingAttachments(a);
		mix.setBillingItems(i);
		mix.setBillingSources(findAllCustom);
		mix.setBillingMarine(findAllMarine);
		mix.setSordersGkey(sorders_gkey);
		return mix;
	}
}
