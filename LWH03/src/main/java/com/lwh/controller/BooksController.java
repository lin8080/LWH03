package com.lwh.controller;

import com.google.gson.GsonBuilder;
import com.lwh.pojo.Bookadmin;
import com.lwh.pojo.PageHelper;
import com.lwh.pojo.Result;
import com.lwh.service.BooksService;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.format.Alignment;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/books")
public class BooksController {

    @Autowired
    BooksService booksService;

    // 显示图书列表方法一    -- 赋值给model，前端直接调用，返回链接地址
//    @RequestMapping("/listBooks")
//    public String listBooks(Model model) {
//        List<Bookadmin> bb = booksService.list();
//        model.addAttribute("bb", bb);
//        return "listBooks";
//    }

    // 显示图书列表方法二    -- 通过ModelAndView返回数据，返回ModelAndView
//    @RequestMapping("/listBooks")
//    public ModelAndView listBooks(){
//        ModelAndView mav = new ModelAndView();
//        List<Bookadmin> bb = booksService.list();
//        mav.addObject("bb",bb);
//        mav.setViewName("listBooks");
//        return mav;
//    }

    @RequestMapping("/listBooks")
    public String listBooks(){
        return "listBooks";
     }

    // 显示图书列表方法三   -- 返回直接json格式，返回对象（对象转换json格式）
    @RequestMapping(value = "/booksList")
    @ResponseBody
    public String showConsumeRecordlList(Bookadmin bookadmin, HttpServletRequest request) {
        PageHelper<Bookadmin> pageHelper = new PageHelper<Bookadmin>();

        // 统计总记录数
        Integer total = booksService.getTotal(bookadmin);
        pageHelper.setTotal(total);

        // 查询当前页实体对象
        List<Bookadmin> list = booksService.getConsumerRecordListPage(bookadmin);
        pageHelper.setRows(list);

        return new GsonBuilder().serializeNulls().create().toJson(pageHelper);
    }

    //    修改数据（两部分）
    //    第一步：更新图书，先通过bid找到图书，并列在/updatepage/{bid}页面上，
    @RequestMapping("/updatePage/{bid}")
    @ResponseBody
    public Bookadmin updatepage(@PathVariable("bid") int bid){
        return booksService.getBookByBid(bid);
    }
    //    第二步：然后修改即可，在这里点更新提交数据给后端
    @RequestMapping(value = "/update/{bid}")
    @ResponseBody
    public Result update(Bookadmin bookadmin,@PathVariable("bid") Integer bid){
        bookadmin.setBid(bid);
        try {
            booksService.update(bookadmin);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "修改失败");
    }

    // 删除图书数据
    @RequestMapping("/delete/{bid}")
    @ResponseBody
    public Result deleteBooksByBid(@PathVariable("bid") Integer bid){
        try {
            booksService.deleteBookByBid(bid);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }

    // 添加数据
    @ResponseBody
    @RequestMapping(value = "/addBooks")
    public Result addBooks(Bookadmin bookadmin){
        try {
            booksService.addBooks(bookadmin);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "保存失败");
    }

    // 删除图书数据
    @RequestMapping("/deletes/{bids}")
    @ResponseBody
    public Result deleteBooksByBids(@PathVariable("bids") Integer[] bids){
        try {
            booksService.deleteBookByBids(bids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除失败");
    }



    // jxl导出
    @ResponseBody
    @RequestMapping("/out")
    public Result Export (){
        String relativelyPath = System.getProperty("user.dir");
        String fold = relativelyPath.substring(0, relativelyPath.lastIndexOf("\\"));


        List<Bookadmin> list = booksService.list();

        if(list != null && list.size()>0){
            try{

                //FileOutputStream out = new FileOutputStream(f);
                // 指定导出数据的路径
                Long time = new Date().getTime();
                String filepath =fold+"//OweMaterial";
                File fpath = new File(filepath);
                fpath.mkdir();
                String filename = fold+"//OweMaterial/"+"测试"+"_"+time.toString()+".xls";
                File f = new File(filename);
                if(!f.exists()){
                    f.createNewFile();
                }
                OutputStream out = new FileOutputStream(f);
                /** **********创建工作簿************ */
                WritableWorkbook wwb = Workbook.createWorkbook(out);
                //创建Excel工作表 指定名称和位置
                WritableSheet ws = wwb.createSheet("图书Excel",0);

                /** ************设置单元格字体************** */
                WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
                WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);

                /** ************以下设置单元格样式************ */
                // 用于标题居中
                WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
                wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
                wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
                wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
                wcf_center.setWrap(false); // 文字是否换行

                // 用于正文居左
                WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
                wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
                wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
                wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
                wcf_left.setWrap(false); // 文字是否换行

                // 汇总表表头
                String[] summTitils = new String[]{
                        "序号","书名","作者"};

                // 显示汇总表
                for (int i = 0; i < summTitils.length; i++) {
                    ws.addCell(new Label(i, 0,summTitils[i],wcf_center));
                }
                int i = 1; // excel 行号
                for(Bookadmin bookadmin:list)
                {
                    ws.addCell(new Label(0, i, i+"",wcf_left));
                    ws.addCell(new Label(1, i, bookadmin.getBookName()+"",wcf_left));
                    ws.addCell(new Label(2, i, bookadmin.getAuthor()+"",wcf_left));
                    i++;
                }


                //写入工作表
                wwb.write();
                wwb.close();
                out.flush();
                out.close();
                String returnstr = "导出成功，文件已经生成。请查看"+filename;
                Runtime runtime=Runtime.getRuntime();
                try{
                    runtime.exec("cmd /c start "+filename);
                }catch(Exception e){
                    return new Result(false, "导出失败");
                }
                return new Result(true, returnstr);
            }catch (Exception e) {
                // TODO: handle exception
                return new Result(false, "导出失败");
            }
        }else{
            return new Result(false, "没有任何要导出的数据");

        }
    }

    @ResponseBody
    @RequestMapping("/import")
    public Result importFile(MultipartFile file) {
        List<Bookadmin> list = new ArrayList<Bookadmin>();
        try {
            // 1.获取用户上传的文件
            Workbook workbook = Workbook.getWorkbook(file.getInputStream());
            // 2.获取工作簿sheet
            Sheet sheet = workbook.getSheet(0);
            // 3.获取总行数
            int rows = sheet.getRows();
            for (int i = 1; i < rows; i++) {
                Bookadmin bookadmin = new Bookadmin();
                String bookName = sheet.getCell(1, i).getContents();
                String author = sheet.getCell(2, i).getContents();

                if (bookName.equals("") || author.equals("")) {
                    return new Result(true, "导入失败，文件存在空数据不允许上传！");

                }

                bookadmin.setBookName(bookName);
                bookadmin.setAuthor(author);

                list.add(bookadmin);

            }
            // 4.添加到数据库中
            booksService.saveAll(list);
            // 5.关闭资源
            workbook.close();
            return new Result(true, "导入成功");
        }catch(Exception e){
            return new Result(false, "导入失败");
        }

    }
}