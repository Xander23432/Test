package com.realnet.ncso.repository1;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.TarrifItem;

@Repository
public interface tarrifItemRepository extends JpaRepository<TarrifItem,Long>{
	Optional<TarrifItem> findByItemCode(String itemCode);
	@Query(value="select * from tariff_item where "
			+ "active = 'Y' ORDER BY item_code",nativeQuery=true)
	Page<TarrifItem> getAllTarrifItem(Pageable page);
	@Query(value="select * from tariff_item where "
			+ "active = 'Y' and invoice_type=?1 ORDER BY item_code",nativeQuery=true)
	List<TarrifItem> getAllTerrifItemFromItemCode(Pageable pageable,@Param("itemCode") String itemCode);
}
