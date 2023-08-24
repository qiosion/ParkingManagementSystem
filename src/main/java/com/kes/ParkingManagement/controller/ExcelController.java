package com.kes.ParkingManagement.controller;


import com.kes.ParkingManagement.dto.CarDTO;
import com.kes.ParkingManagement.repository.CarRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    CarRepository carRepository;

    // poi 5.2.2는 안되고 5.0.0 에서 import 되네..
    Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @GetMapping("/parkingLot")
    public ResponseEntity<InputStreamResource> downloadParkingLotStatus (HttpServletResponse response) throws IOException {
        // xls 파일
        // Workbook workbook = new HSSFWorkbook();

        // xlsx 파일
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("전체 주차 목록");
            int rowNo = 0;

            // 스타일, 폰트 설정
            CellStyle headStyle = workbook.createCellStyle();
            headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//            Font font = workbook.createFont();
//            font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
//            font.setFontHeightInPoints((short)13);
//            headStyle.setFont(font);

            // 헤더(칼럼명) 설정
            Row headerRow = sheet.createRow(rowNo++);
            headerRow.createCell(0).setCellValue("주차 번호");
            headerRow.createCell(1).setCellValue("차 번호");
            headerRow.createCell(2).setCellValue("주차 상태");
            headerRow.createCell(3).setCellValue("입차 시각");
            headerRow.createCell(4).setCellValue("출차 시각");
            headerRow.createCell(5).setCellValue("주차 시간");
            headerRow.createCell(6).setCellValue("금액");

            for (int i = 0; i < 7; i++) {
                headerRow.getCell(i).setCellStyle(headStyle);
            }

            // 데이터 불러오기
            List<CarDTO> list = carRepository.viewAll();

            for (CarDTO carDTO : list) {
                String entryTime = (carDTO.getEntryTime() != null) ? carDTO.getEntryTime().toString() : "";
                String exitTime = (carDTO.getExitTime() != null) ? carDTO.getExitTime().toString() : "";
                String parkingDuration = (carDTO.getParkingDuration() != null) ? carDTO.getParkingDuration() + "분" : "";
                String parkingFee = (carDTO.getParkingFee() != null) ? carDTO.getParkingFee() + "원" : "";

                Row row = sheet.createRow(rowNo++);
                row.createCell(0).setCellValue(carDTO.getParkNumber()); // 주차번호
                row.createCell(1).setCellValue(carDTO.getCarNumber()); // 차번호
                row.createCell(2).setCellValue(carDTO.getState()); // 주차상태
                row.createCell(3).setCellValue(entryTime); // 입차시각
                row.createCell(4).setCellValue(exitTime); // 출차시각
                row.createCell(5).setCellValue(parkingDuration); // 주차시간
                row.createCell(6).setCellValue(parkingFee); // 금액
            }

            // 칼럼 너비
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 3000);
            sheet.setColumnWidth(3, 6000);
            sheet.setColumnWidth(4, 6000);
            sheet.setColumnWidth(5, 6000);
            sheet.setColumnWidth(6, 2000);

            // 엑셀 파일 생성
            File tmpFile = File.createTempFile("TMP~", ".xlsx");

            try (OutputStream fos = new FileOutputStream(tmpFile)) {
                workbook.write(fos);
            }

            InputStream res = new FileInputStream(tmpFile) {
                @Override
                public void close() throws IOException {
                    super.close();
                    if (tmpFile.delete()) {
                        logger.info("임시 파일 삭제 완료");
                    }
                }
            };
            return ResponseEntity.ok()
                    .contentLength(tmpFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment;filename=carlist.xlsx")
                    .body(new InputStreamResource(res));
        }
    }
}
