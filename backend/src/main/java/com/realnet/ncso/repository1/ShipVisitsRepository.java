package com.realnet.ncso.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.ShipVisits;
@Repository
public interface ShipVisitsRepository extends JpaRepository<ShipVisits, String>{

}
