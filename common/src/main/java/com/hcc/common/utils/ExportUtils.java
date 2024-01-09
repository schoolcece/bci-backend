package com.hcc.common.utils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Description: 数据导出工具
 *
 * @Author: hcc
 * @Date: 2024/1/3
 */
public class ExportUtils {

    public static void setExportHeaderForExcel(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
    }
}
