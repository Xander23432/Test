package com.realnet.icon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.icon.entity.WatchListEntity;

@Repository
public interface WatchListRepository  extends JpaRepository<WatchListEntity, Integer>{
	

    @Query(value="SELECT count(*) from watchlist where object_id=?1",nativeQuery = true)
    int countWatchlist(int projectId);
    
    @Query(value="SELECT count(*) from watchlist where object_id=?1 AND created_by=?2",nativeQuery = true)
    int IsWatchlist(int projectId, Long userId);

}
