package com.child.service;

import com.child.web.dto.ChildDTO;

import java.util.List;

public interface IChildService {
    ChildDTO addChild(ChildDTO childDTO);

    List<ChildDTO> getAllChildren();

    ChildDTO activateChild(String qrCode);
}
