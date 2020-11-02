package com.msp.console;

import com.msp.console.model.StudentModel;
import com.msp.console.model.SubjectModel;
import com.msp.console.util.StringUtils;
import com.msp.console.util.SubjectEnum;

import java.util.Calendar;
import java.util.Scanner;

import static com.msp.console.util.DateUtils.convertStringtoDate;
import static com.msp.console.util.DateUtils.isValidDate;
import static com.msp.console.util.FormatDate.FORMAT_6;
import static com.msp.console.util.StringUtils.*;
import static com.msp.console.util.SubjectEnum.values;
import static java.util.Calendar.YEAR;

public class MainClass {

    private static final Scanner sc = new Scanner(System.in);
    private static StudentModel[] students;

    public static void main(String[] args) {
        System.out.println("--- Chào mừng tới ứng dụng quản lý học sinh ---");
        int selection = 0;

        do {
            System.out.println("Chọn chức năng: ");
            System.out.println("1. Thêm học sinh");
            System.out.println("2. Sửa học sinh");
            System.out.println("3. Xóa học sinh");
            System.out.println("4. Tìm kiếm học sinh (theo mã hoặc tên)");
            System.out.println("5. Liệt kê danh sách học sinh theo lớp");
            System.out.println("6. Tìm kiếm top 10 học sinh xuất sắc nhất theo năm");
            System.out.println("7. Tìm kiếm các học sinh có điểm trung bình các môn nhỏ hơn 5");
            System.out.println("#. Thoát");

            while (!sc.hasNextInt()) {
                System.out.println("Lựa chọn không hợp lệ!");
                sc.next();
            }
            selection = sc.nextInt();

            switch (selection) {
                case 1:
                    createStudent(null);
                    break;
                case 2:
                    editStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    findStudentByCodeOrName();
                    break;
                case 5:
                    findStudentsByClass();
                    break;
                case 6:
                    findTopTen();
                    break;
                case 7:
                    findByAverage();
                    break;
            }
        } while (selection > 0 && selection < 8);
    }

    private static boolean isStudentCodeExist(String code) {
        if (students == null || students.length < 1 || isNullOrBlank(code)) return false;
        for (StudentModel student : students) {
            if (!isNullOrBlank(student.getCode()) && StringUtils.compareString(student.getCode(), code)) {
                return true;
            }
        }
        return false;
    }

    private static void createStudent(StudentModel foundModel) {
        StudentModel student = foundModel == null ? new StudentModel() : foundModel;
        System.out.println("--- Nhập vào thông tin học sinh ---");
        System.out.println("Mã học sinh: ");
        sc.nextLine();

        String code;
        do {
            code = sc.nextLine();
            if (isStudentCodeExist(code)) {
                System.out.println("Mã học sinh đã tồn tại!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        if (foundModel != null && isNullOrBlank(code)) {
            student.setCode(foundModel.getCode());
        } else {
            student.setCode(code);
        }

        System.out.println("Tên học sinh: ");
        String name = sc.nextLine();
        if (foundModel != null && isNullOrBlank(name)) {
            student.setFullName(foundModel.getFullName());
        } else {
            student.setFullName(name);
        }

        System.out.println("Ngày sinh (dd/mm/yyyy): ");
        String dateOfBirth;
        do {
            dateOfBirth = sc.nextLine();
            if (!isNullOrBlank(dateOfBirth) && !isValidDate(dateOfBirth, FORMAT_6)) {
                System.out.println("Sai định dạng ngày tháng!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        if (foundModel != null && isNullOrBlank(dateOfBirth)) {
            student.setDateOfBirth(foundModel.getDateOfBirth());
        } else {
            student.setDateOfBirth(convertStringtoDate(dateOfBirth, FORMAT_6));
        }

        System.out.println("Mã lớp: ");
        String classCode = sc.nextLine();
        if (foundModel != null && isNullOrBlank(classCode)) {
            student.setClassCode(foundModel.getClassCode());
        } else {
            student.setClassCode(classCode);
        }

        if (foundModel == null) {
            createSubjectsForStudent(student);
            addStudentToArray(student);
        }

        System.out.println("==>> " + (foundModel == null ? "Thêm" : "Sửa") + " học sinh thành công <<===");
    }

    private static void createSubjectsForStudent(StudentModel student) {
        System.out.println("--- Nhập vào thông tin bảng điểm học sinh ---");
        System.out.println("Nhập số năm:");
        int subjectLen = values().length;
        float mark = 0;
        float sum = 0;

        while (!sc.hasNextInt()) {
            System.out.println("Số năm không hợp lệ!");
            sc.next();
        }
        int numberOfYear = sc.nextInt();
        if (numberOfYear < 1) return;

        student.setSubjects(new SubjectModel[numberOfYear][SubjectEnum.values().length]);

        for (int year = 0; year < numberOfYear; year++) {
            System.out.println("-- Năm thứ " + (year + 1));

            for (int i = 0; i < subjectLen; i++) {
                System.out.println(values()[i]); //Tên môn học

                while (true) {
                    while (!sc.hasNextFloat()) {
                        System.out.println("Điểm phải ở dạng số thập phân");
                        sc.next();
                    }
                    mark = sc.nextFloat();
                    if (mark < 0 || mark > 10) {
                        System.out.println("Điểm phải nằm trong khoảng 0-10!");
                    } else {
                        break;
                    }
                }
                student.getSubjects()[year][i] = new SubjectModel(values()[i].name(), mark);
                sum += mark;
            }
        }
        student.setAvgMark(Math.round(sum / (numberOfYear * 9) * 100) / 100.0f);
    }

    private static void addStudentToArray(StudentModel student) {
        if (students == null || students.length < 1) {
            students = new StudentModel[]{student};
            return;
        }

        int len = students.length;
        StudentModel[] tempArr = new StudentModel[len + 1];

        for (int i = 0; i <= len; i++) {
            if (i == len) {
                tempArr[i] = student;
            } else {
                tempArr[i] = students[i];
            }
        }

        students = tempArr;
        sortArrAsc();
    }

    private static Integer getStudent(String title) {
        if (isArrEmpty()) return null;

        System.out.println(title);
        System.out.println("Chọn học sinh theo danh sách dưới đây:");
        int len = students.length;
        for (int i = 0; i < len; i++) {
            System.out.println(i + ". " + students[i].toString());
        }

        int selection = sc.nextInt();
        if (selection < 0 || selection > len - 1) {
            System.out.println("==> Không có học sinh nào <==");
            return null;
        }

        return selection;
    }

    private static void editStudent() {
        Integer foundIndex = getStudent("--- Sửa thông tin học sinh ---");
        if (foundIndex != null) {
            createStudent(students[foundIndex]);
        }
    }

    private static void deleteStudent() {
        Integer foundIndex = getStudent("--- Xóa thông tin học sinh ---");
        if (foundIndex != null) {
            int len = students.length;
            StudentModel[] tempArr = new StudentModel[len - 1];
            int index = 0;

            for (int i = 0; i < len; i++) {
                if (i == foundIndex) {
                    continue;
                }
                tempArr[index] = students[i];
                index++;
            }

            students = tempArr;
        }
    }

    private static void findStudentByCodeOrName() {
        if (isArrEmpty()) return;

        System.out.println("--- Tìm kiếm học sinh bằng mã hoặc tên ---");
        System.out.println("Nhập mã hoặc tên cần tìm:");
        sc.nextLine();
        String val = sc.nextLine();
        if (isNullOrBlank(val)) {
            System.out.println("==> Không có học sinh nào <==");
            return;
        }

        boolean isHasStudent = false;
        for (StudentModel student : students) {
            if ((!isNullOrBlank(student.getFullName()) && contains(student.getFullName(), val)) ||
                    (!isNullOrBlank(student.getCode()) && contains(student.getCode(), val))) {
                System.out.println(student.toString());
                isHasStudent = true;
            }
        }

        if (!isHasStudent) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    private static void findStudentsByClass() {
        if (isArrEmpty()) return;

        System.out.println("--- Liệt kê danh sách học sinh của một lớp ----");
        System.out.println("Nhập mã lớp cần tìm:");
        sc.nextLine();
        String val = sc.nextLine();
        if (isNullOrBlank(val)) {
            System.out.println("==> Không có học sinh nào <==");
            return;
        }

        boolean isHasStudent = false;
        for (StudentModel student : students) {
            if (!isNullOrBlank(student.getClassCode()) && compareString(student.getClassCode(), val)) {
                System.out.println(student.toString());
                isHasStudent = true;
            }
        }

        if (!isHasStudent) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    private static void sortArrAsc() {
        int len = students.length;
        StudentModel temp;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (students[i].getAvgMark() < students[j].getAvgMark()) {
                    temp = students[i];
                    students[i] = students[j];
                    students[j] = temp;
                }
            }
        }
    }

    private static void findTopTen() {
        if (isArrEmpty()) return;
        System.out.println("--- Tìm kiếm top 10 học sinh xuất sắc nhất ----");
        int amount = 0;
        for (StudentModel student : students) {
            if (amount < 10) {
                System.out.println(student.toString());
                amount++;
            }
        }

        if (amount == 0) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    private static void findByAverage() {
        if (isArrEmpty()) return;
        System.out.println("--- Danh sách học sinh có điểm trung binh các môn nhỏ hơn 5 ----");
        for (StudentModel student : students) {
            if (student.getAvgMark() < 5) {
                System.out.println(student.toString());
            }
        }
    }

    private static boolean isArrEmpty() {
        if (students == null || students.length < 1) {
            System.out.println("==> Không có học sinh nào <==");
            return true;
        }

        return false;
    }
}
