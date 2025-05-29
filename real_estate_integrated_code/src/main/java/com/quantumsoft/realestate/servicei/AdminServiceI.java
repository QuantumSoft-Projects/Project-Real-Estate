package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.dto.ResetPasswordDto;
import com.quantumsoft.realestate.entity.Admin;
import com.quantumsoft.realestate.enums.PropertyStatus;

public interface AdminServiceI {

    public String register(Admin admin);

    public String sendOtp(String email);

    public String resetPassword(ResetPasswordDto resetPasswordDto);

   public void savedAdminStatus(Admin admin);

   public String propertyApproval(Long id, String propertyStatus);
}
