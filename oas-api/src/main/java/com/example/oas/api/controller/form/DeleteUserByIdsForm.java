package com.example.oas.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Schema(description = "删除用户表单")
@Data
public class DeleteUserByIdsForm {
    @NotEmpty(message = "ids不能为空")
    @Schema(description = "用户ID")
    private Integer[] ids;
}
