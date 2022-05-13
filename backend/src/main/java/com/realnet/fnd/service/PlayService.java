package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.comm.entity.PlayEntity;
public interface PlayService {

	public Page<PlayEntity> getlist(Pageable page);
    public PlayEntity createCollageStudent(PlayEntity data);
    public PlayEntity getid(int id);
    public PlayEntity updateById(int id, PlayEntity collegePortalEntity);
    public boolean deleteById(int id);
    public PlayEntity getwf_instance_id(int id);
    public PlayEntity updateBywf_instance_id(int id, PlayEntity collegePortalEntity);
}
