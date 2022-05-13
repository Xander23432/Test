package com.realnet.wfb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.wfb.entity.Rn_Fb_Line;

@Repository
public interface Rn_Fb_Line_Repository extends JpaRepository<Rn_Fb_Line, Integer> {
	// for pagination
	Page<Rn_Fb_Line> findAll(Pageable p);

	// FIND LINES BY HEADER ID
	@Query(value = "SELECT * from rn_fb_lines WHERE HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLinesByHeaderId(@Param("header_id") int rn_fb_header_id);

	@Query(value = "SELECT MAX(SECTION_NUM) from rn_fb_lines WHERE HEADER_ID=:header_id", nativeQuery = true)
	int findMaxSectionNumber(@Param("header_id") int rn_fb_header_id);

	@Query(value = "SELECT MAX(SEQ) from rn_fb_lines WHERE HEADER_ID=:header_id", nativeQuery = true)
	int findMaxSeqOfFields(@Param("header_id") int rn_fb_header_id);

	@Query(value = "SELECT MAX(SEQ) from rn_fb_lines WHERE HEADER_ID=:header_id AND FORM_TYPE=:form_type AND SECTION_NUM=:section_number", nativeQuery = true)
	int findMaxSeqWithFormTypeAndSectionNumber(@Param("header_id") int header_id,
			@Param("section_number") int sectionNumber, @Param("form_type") String formType);

	@Query(value = "SELECT MAX(BUTTON_NUM) from rn_fb_lines WHERE HEADER_ID=:header_id", nativeQuery = true)
	int findMaxButtonNumber(@Param("header_id") int rn_fb_header_id);

	// FIND BUTTON LIST ( need mod)
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD = 'BUTTON' AND FIELD_NAME = 'SUBMIT' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findButtonList(@Param("header_id") int rn_fb_header_id);

	// FIND EXTRA BUTTON LIST
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD = 'BUTTON' AND FIELD_NAME != 'SUBMIT' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findExtraButton(@Param("header_id") int rn_fb_header_id);

	// HEADER SECTION VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD = 'SECTION' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findSection(@Param("header_id") int rn_fb_header_id);

	// HEADER TYPE FORM SECTION FIELDS ( NEED MODIFICATION)
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1!='PRI' AND DATA_TYPE!='DATETIME' AND DATA_TYPE!='INT' AND TYPE2='HEADER' AND DATA_TYPE!='N' AND SECTION_NUM =:section_num AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findSectionFields(@Param("section_num") int section_num, @Param("header_id") int rn_fb_header_id);

	// HEADER SECTION FIELD (FOR FRONT END)
	// NEED MODIFICATION
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD != 'SECTION' AND TYPE_FIELD != 'BUTTON' AND TYPE_FIELD!='ID' AND KEY1!='PRI' AND KEY1!='HID' AND SECTION_NUM =:section_num AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findSectionFieldsForFrontEnd(@Param("section_num") int section_num,
			@Param("header_id") int rn_fb_header_id);

	// PK VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1 = 'PRI' AND DATA_TYPE != 'N' AND TYPE2 = 'HEADER' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findPrimaryKeyField(@Param("header_id") int rn_fb_header_id);

	// VARCHAR VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1!='PRI' AND DATA_TYPE!='DATETIME' AND DATA_TYPE!='INT' AND DATA_TYPE!='LONGTEXT' AND DATA_TYPE!='DOUBLE' AND DATA_TYPE!='FLOAT' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findVarcharFields(@Param("header_id") int rn_fb_header_id);

	// INT VALUES (NOT PK)
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='INT' AND KEY1!='PRI' AND  DATA_TYPE != 'N' AND TYPE2 = 'HEADER' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findIntegerFields(@Param("header_id") int rn_fb_header_id);

	// DATETIME VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='DATETIME' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findDateTimeFields(@Param("header_id") int rn_fb_header_id);

	// LONGTEXT VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='LONGTEXT' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLongtextFields(@Param("header_id") int rn_fb_header_id);

	// DOUBLE VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='DOUBLE' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findDoubleFields(@Param("header_id") int rn_fb_header_id);

	// ------- ONLY LINE FORM ------------ //

	// ONLY LINE TYPE FORM SECTION FIELDS ( NEED MODIFICATION)
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1!='PRI' AND TYPE2='LINE' AND DATA_TYPE!='N' AND SECTION_NUM =:section_num AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findOnlyLineSectionFields(@Param("section_num") int section_num, @Param("header_id") int header_id);
	// H-L LINE SECTION FIELD (FOR FRONT END)
	
//	// NEED MODIFICATION
//	@Query(value = "SELECT * from RN_FB_LINES WHERE TYPE_FIELD != 'SECTION' AND TYPE_FIELD != 'BUTTON' AND TYPE2 ='LINE' AND HEADER_ID=:header_id", nativeQuery = true)
//	List<Rn_Fb_Line> findOnlyLineSectionFieldsForFrontEnd(@Param("header_id") int header_id);
	
	// ------- H-L LINE TABLE VALUE ----------- //
	
	// LINE PK VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1 = 'PRI' AND DATA_TYPE != 'N' AND TYPE2 = 'LINE' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLinePrimarkKeyField(@Param("header_id") int rn_fb_header_id);

	// LINE SECTION VALUE
	// @Query(value = "SELECT * from RN_FB_LINES WHERE KEY1!='PRI' AND DATA_TYPE =
	// 'N' AND TYPE_FIELD = 'LINE_SECTION' AND TYPE2 ='LINE' AND
	// HEADER_ID=:header_id", nativeQuery = true)
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE = 'N' AND TYPE_FIELD = 'LINE_SECTION' AND TYPE2 ='LINE' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLineSection(@Param("header_id") int rn_fb_header_id); // only used in header line form

	// H-L LINE SECTION FIELD (FOR FRONT END)
	// NEED MODIFICATION
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD != 'LINE_SECTION' AND TYPE_FIELD != 'BUTTON' AND TYPE2 ='LINE' AND KEY1 != 'hid' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLineSectionFieldsForFrontEnd(@Param("header_id") int rn_fb_header_id); // used in hl form

	// LINE VARCHAR VALUES
	@Query(value = "SELECT * from rn_fb_lines WHERE KEY1!='PRI' AND DATA_TYPE!='INT' AND DATA_TYPE != 'N'  AND TYPE2 ='LINE' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLineVarcharFields(@Param("header_id") int rn_fb_header_id);

	// LINE INT VALUES (NOT PK)
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='INT' AND KEY1!='PRI' AND  DATA_TYPE != 'N' AND TYPE2 = 'LINE' AND AND KEY1='HID' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLineIntegerFields(@Param("header_id") int rn_fb_header_id);

//	// DATETIME VALUES
//	@Query(value = "SELECT * from RN_FB_LINES WHEREWHERE DATA_TYPE='DATETIME' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
//	List<Rn_Fb_Line> findLineDateTimeFields(@Param("header_id")int rn_fb_header_id);

//	// LONGTEXT VALUES
//	@Query(value = "SELECT * from RN_FB_LINES WHEREWHERE DATA_TYPE='LONGTEXT' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
//	List<Rn_Fb_Line> findLineLongtextFields(@Param("header_id")int rn_fb_header_id);

//	// DOUBLE VALUES
//	@Query(value = "SELECT * from RN_FB_LINES WHEREWHERE DATA_TYPE='DOUBLE' AND TYPE2 = 'HEADER' AND DATA_TYPE!='N' AND HEADER_ID=:header_id", nativeQuery = true)
//	List<Rn_Fb_Line> findLineDoubleFields(@Param("header_id")int rn_fb_header_id);

	// ===========\

	// get only header fields (in OH, HL, OL, ML type form)
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD != 'SECTION'  AND TYPE_FIELD != 'HEADER_NAME' AND TYPE_FIELD != 'BUTTON' AND TYPE2 = 'HEADER' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findHeaderFields(@Param("header_id") int rn_fb_header_id);

	// get only line fields (in OH, HL, OL, ML type form)
	@Query(value = "SELECT * from rn_fb_lines WHERE TYPE_FIELD != 'SECTION'  AND TYPE_FIELD != 'HEADER_NAME' AND TYPE_FIELD != 'BUTTON' AND DATA_TYPE != 'N' AND TYPE2 = 'LINE' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findLineFields(@Param("header_id") int rn_fb_header_id);

	// ------- M-L LINE TABLE VALUE ----------- //
	@Query(value = "SELECT * from rn_fb_lines WHERE DATA_TYPE='INT' AND KEY1='HID' AND  DATA_TYPE != 'N' AND TYPE2 = 'HEADER' AND HEADER_ID=:header_id", nativeQuery = true)
	List<Rn_Fb_Line> findHidIntegerFields(@Param("header_id") int rn_fb_header_id);

	@Query(value = "SELECT * from rn_fb_lines WHERE HEADER_ID=:header_id AND SECTION_NUM=:section_num", nativeQuery = true)
	List<Rn_Fb_Line> deleteSection(@Param("header_id") int header_id, @Param("section_num") int sectionNumber);
}
