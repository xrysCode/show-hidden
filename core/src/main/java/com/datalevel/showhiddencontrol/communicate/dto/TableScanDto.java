package com.datalevel.showhiddencontrol.communicate.dto;

import com.datalevel.showhiddencontrol.sdk.TableInfoDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TableScanDto {
    String authCode;
    List<TableInfoDto> tableInfoList;

    public TableScanDto(String authCode, List<TableInfoDto> tableInfoList) {
        this.authCode = authCode;
        this.tableInfoList = tableInfoList;
    }

}
