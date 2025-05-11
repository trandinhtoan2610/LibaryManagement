-- Xóa database cũ nếu tồn tại và tạo mới (để đảm bảo bắt đầu sạch)
DROP DATABASE IF EXISTS library_management;
CREATE DATABASE library_management;
USE library_management;

-- Create tables
CREATE TABLE `Role` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE `Employee` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `firstName` VARCHAR(255) NOT NULL,
                            `lastName` VARCHAR(255) NOT NULL,
                            `gender` ENUM('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                            `username` VARCHAR(255) UNIQUE NOT NULL,
                            `password` VARCHAR(255) NOT NULL, -- Trong thực tế nên hash password
                            `roleId` BIGINT NOT NULL,
                            `phone` VARCHAR(20) NOT NULL,
                            `address` VARCHAR(255) NOT NULL,
                            `salary` BIGINT NOT NULL,
                            `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Reader` (
                          `id` VARCHAR(10) PRIMARY KEY,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `gender` ENUM('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                          `phone` VARCHAR(10) NOT NULL,
                          `address` VARCHAR(255) NOT NULL,
    -- `complianceCount` INT NOT NULL DEFAULT 3, -- Cột này sẽ bị xóa ở cuối
                          `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Author` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `quantity` INT NOT NULL DEFAULT 0, -- Số lượng đầu sách sẽ được cập nhật
                          `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Category` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(255) NOT NULL,
                            `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Publisher` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `name` VARCHAR(255) NOT NULL,
                             `phone` VARCHAR(10) NOT NULL,
                             `address` VARCHAR(255) NOT NULL,
                             `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Book` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` VARCHAR(255) NOT NULL,
                        `categoryId` BIGINT NOT NULL,
                        `authorId` BIGINT NOT NULL,
                        `publisherId` BIGINT NOT NULL,
                        `quantity` INT NOT NULL, -- Số lượng tổng ban đầu (sẽ được cập nhật từ phiếu nhập)
                        `borrowedQuantity` INT NOT NULL DEFAULT 0, -- Số lượng đang được mượn (sẽ được cập nhật)
                        `unitprice` BIGINT NOT NULL,
                        `yearOfpublication` YEAR,
                        `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Supplier` (
                            `id` VARCHAR(10) NOT NULL PRIMARY KEY,
                            `name` VARCHAR(255) NOT NULL,
                            `phone` VARCHAR(10) NOT NULL,
                            `address` VARCHAR(255) NOT NULL,
                            `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Borrow_in_Sheet` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   `employeeId` BIGINT NOT NULL,
                                   `readerId` VARCHAR(10) NOT NULL,
                                   `borrowedDate` DATETIME NOT NULL,
                                   `duedate` DATETIME NOT NULL,
                                   `actualReturnDate` DATETIME DEFAULT NULL,
                                   `status` ENUM('Đang_Mượn', 'Đã_Trả', 'Phạt') NOT NULL DEFAULT 'Đang_Mượn'
);

CREATE TABLE `BorrowDetails` (
                                 `bookId` BIGINT NOT NULL,
                                 `borrowSheetId` BIGINT NOT NULL,
                                 `quantity` INT NOT NULL DEFAULT 1,
                                 `substatus` ENUM('Đang_Mượn', 'Đã_Trả', 'Mất_Sách', 'Hư_Sách', 'Quá_Hạn') NOT NULL DEFAULT 'Đang_Mượn',
                                 `actualReturnDate` DATETIME DEFAULT NULL,
                                 PRIMARY KEY (`bookId`, `borrowSheetId`)
);

CREATE TABLE `PurchaseOrders` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `supplierId` VARCHAR(10) NOT NULL,
                                  `employeeId` BIGINT NOT NULL,
                                  `totalAmount` DECIMAL(12,2) NOT NULL DEFAULT 0,
                                  `buyDate` DATETIME NOT NULL,
                                  `status` ENUM('Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy') NOT NULL DEFAULT 'Đang_Chờ'
);

CREATE TABLE `PurchaseOrderDetails` (
                                        `purchaseOrderId` BIGINT NOT NULL,
                                        `bookId` BIGINT NOT NULL,
                                        `quantity` INT NOT NULL DEFAULT 1,
                                        `unitPrice` DECIMAL(10,2) NOT NULL,
                                        `SubTotal` DECIMAL(12,2) NOT NULL,
                                        PRIMARY KEY (`purchaseOrderId`, `bookId`)
);

CREATE TABLE `Penalty` (
                           `id` VARCHAR(10) PRIMARY KEY,
                           `penaltyDate` DATETIME NOT NULL,
                           `totalamount` DECIMAL(10,2) NOT NULL,
                           `status` ENUM ('Đã_Thanh_Toán', 'Chưa_Thanh_Toán') NOT NULL DEFAULT 'Chưa_Thanh_Toán',
                           `employeeID` BIGINT,
                           `payDate` DATETIME DEFAULT NULL,
                           FOREIGN KEY (`employeeID`) REFERENCES `Employee`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `PenaltyDetails` (
                                  `penaltyId` VARCHAR(10) NOT NULL,
                                  `bookId` BIGINT NOT NULL,
                                  `borrowId` BIGINT NOT NULL,
                                  `name` VARCHAR(255),
                                  `subamount` DECIMAL(10,2) NOT NULL,
                                  `bookQuantity` INT NOT NULL,
                                  PRIMARY KEY (`penaltyId`, `bookId`, `borrowId`),
                                  FOREIGN KEY (`penaltyId`) REFERENCES `Penalty`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Add foreign key constraints
ALTER TABLE `Employee` ADD FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Book` ADD FOREIGN KEY (`categoryId`) REFERENCES `Category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `Book` ADD FOREIGN KEY (`authorId`) REFERENCES `Author` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `Book` ADD FOREIGN KEY (`publisherId`) REFERENCES `Publisher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`readerId`) REFERENCES `Reader` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`borrowSheetId`) REFERENCES `Borrow_in_Sheet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`supplierId`) REFERENCES `Supplier` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`purchaseOrderId`) REFERENCES `PurchaseOrders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;


-- #####################################################################
-- ### INSERT SAMPLE DATA ###
-- #####################################################################

-- Insert Roles
INSERT INTO `Role` (`name`) VALUES
                                ('Admin'),
                                ('Staff'),
                                ('Employee');

-- Insert Employees
INSERT INTO `Employee` (`firstName`, `lastName`, `gender`, `username`, `password`, `roleId`, `phone`, `address`, `salary`, `isActive`) VALUES ('Admin',
                                                                                                                                               '',
                                                                                                                                               'Nam',
                                                                                                                                               'admin',
                                                                                                                                               'admin',
                                                                                                                                               1,
                                                                                                                                               '0912345670',
                                                                                                                                               '123 Đường ABC, Quận 1, TP.HCM',
                                                                                                                                               0,
                                                                                                                                               1),
                                                                                                                                           ('Nguyễn', 'Văn Thành', 'Nam', 'thanhnv_admin', 'hashed_pw', 1, '0912345670', '123 Đường ABC, Quận 1, TP.HCM', 15000000, 1),
                                                                                                                                           ('Trần', 'Thị Lan', 'Nữ', 'lantt_staff', 'hashed_pw', 2, '0987654321', '456 Đường XYZ, Quận 3, TP.HCM', 9000000, 1),
                                                                                                                                           ('Lê', 'Hoàng Minh', 'Nam', 'minhlh_emp', 'hashed_pw', 3, '0912345678', '789 Đường KLM, Quận Bình Thạnh, TP.HCM', 7500000, 1),
                                                                                                                                           ('Phạm', 'Hoài An', 'Nữ', 'anph_staff', 'hashed_pw', 2, '0911223344', '101 Đường DEF, Quận Gò Vấp, TP.HCM', 9500000, 1),
                                                                                                                                           ('Vũ', 'Tuấn Khải', 'Nam', 'khaivt_emp', 'hashed_pw', 3, '0905678910', '202 Đường GHI, Quận Tân Bình, TP.HCM', 8000000, 1),
                                                                                                                                           ('Chí', 'Văn', 'Nam', 'van', 'van', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1),
                                                                                                                                           ('Võ Duy', 'Thanh', 'Nam', 'thanh', 'thanh', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1),
                                                                                                                                           ('Nhan Chí', 'Phong', 'Nam', 'giaitich1', 'giaitich2', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1),
                                                                                                                                           ('Hoàng', 'Quý', 'Nam', 'hoang', 'hoang', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1),
                                                                                                                                           ('Hoàng', 'Gia Huy', 'Nam', 'huyhg_staff', 'hashed_pw', 2, '0909112233', '303 Đường JKL, Quận Phú Nhuận, TP.HCM', 8800000, 1),
                                                                                                                                           ('Đặng', 'Mỹ Linh', 'Nữ', 'linhdm_emp', 'hashed_pw', 3, '0977889900', '404 Đường MNO, Quận 10, TP.HCM', 7200000, 1),
                                                                                                                                           ('Bùi', 'Quốc Bảo', 'Nam', 'baobq_emp', 'hashed_pw', 3, '0933445566', '505 Đường PQR, Quận Tân Phú, TP.HCM', 7800000, 1),
                                                                                                                                           ('Ngô', 'Phương Thảo', 'Nữ', 'thaonp_staff', 'hashed_pw', 2, '0944556677', '606 Đường STU, Quận 7, TP.HCM', 9200000, 1),
                                                                                                                                           ('Lý', 'Chí Thanh', 'Nam', 'thanhlt_admin', 'hashed_pw', 1, '0966778899', '707 Đường VWX, Quận 2, TP.HCM', 16000000, 1),
                                                                                                                                           ('Mai', 'Thu Trang', 'Nữ', 'trangmt_emp', 'hashed_pw', 3, '0922334455', '808 Đường YZA, Quận 12, TP.HCM', 7000000, 1),
                                                                                                                                           ('Dương', 'Minh Đức', 'Nam', 'ducdm_staff', 'hashed_pw', 2, '0955667788', '909 Đường BCD, Huyện Bình Chánh, TP.HCM', 8500000, 0),
                                                                                                                                           ('Trịnh', 'Ngọc Mai', 'Nữ', 'maitn_emp', 'hashed_pw', 3, '0918765432', '111 Đường EFG, Quận 5, TP.HCM', 7300000, 1),
                                                                                                                                           ('Phan', 'Đăng Khoa', 'Nam', 'khoapd_staff', 'hashed_pw', 2, '0981234567', '222 Đường HIJ, Quận 6, TP.HCM', 9100000, 1);

-- Insert Readers
INSERT INTO `Reader` (`id`, `firstName`, `lastName`, `gender`, `phone`, `address`, `isActive`) VALUES
                                                                                                   ('RD1', 'Nguyễn', 'Văn A', 'Nam', '0911111101', '11 Đường Số 1, Quận 1, TP.HCM', 1),
                                                                                                   ('RD2', 'Trần', 'Thị B', 'Nữ', '0922222202', '22 Đường Số 2, Quận 2, TP.HCM', 1),
                                                                                                   ('RD3', 'Lê', 'Hoàng C', 'Nam', '0933333303', '33 Đường Số 3, Quận 3, TP.HCM', 1),
                                                                                                   ('RD4', 'Phạm', 'Mỹ D', 'Nữ', '0944444404', '44 Đường Số 4, Quận 4, TP.HCM', 1),
                                                                                                   ('RD5', 'Hoàng', 'Gia E', 'Nam', '0955555505', '55 Đường Số 5, Quận 5, TP.HCM', 1),
                                                                                                   ('RD6', 'Đặng', 'Khánh F', 'Nam', '0966666606', '66 Đường Số 6, Quận 6, TP.HCM', 1),
                                                                                                   ('RD7', 'Bùi', 'Thu G', 'Nữ', '0977777707', '77 Đường Số 7, Quận 7, TP.HCM', 1),
                                                                                                   ('RD8', 'Vũ', 'Minh H', 'Nam', '0988888808', '88 Đường Số 8, Quận 8, TP.HCM', 1),
                                                                                                   ('RD9', 'Lý', 'Ngọc I', 'Nữ', '0999999909', '99 Đường Số 9, Quận 9, TP.HCM', 1),
                                                                                                   ('RD10', 'Trương', 'Anh K', 'Nam', '0901000010', '110 Đường Số 10, Quận 10, TP.HCM', 1),
                                                                                                   ('RD11', 'Đinh', 'Tuyết L', 'Nữ', '0902000011', '111 Đường Số 11, Quận 11, TP.HCM', 1),
                                                                                                   ('RD12', 'Ngô', 'Quốc M', 'Nam', '0903000012', '112 Đường Số 12, Quận 12, TP.HCM', 1),
                                                                                                   ('RD13', 'Hà', 'Phương N', 'Nữ', '0904000013', '113 Đường Số 1, Quận Tân Bình, TP.HCM', 1),
                                                                                                   ('RD14', 'Phan', 'Thành O', 'Nam', '0905000014', '114 Đường Số 2, Quận Bình Tân, TP.HCM', 1),
                                                                                                   ('RD15', 'Tạ', 'Diễm P', 'Nữ', '0906000015', '115 Đường Số 3, Quận Phú Nhuận, TP.HCM', 1),
                                                                                                   ('RD16', 'Dương', 'Huy Q', 'Nam', '0907000016', '116 Đường Số 4, Quận Gò Vấp, TP.HCM', 1),
                                                                                                   ('RD17', 'Lương', 'Mai R', 'Nữ', '0908000017', '117 Đường Số 5, Thủ Đức, TP.HCM', 1),
                                                                                                   ('RD18', 'Hoàng', 'Sơn S', 'Nam', '0909000018', '118 Đường Số 6, Bình Chánh, TP.HCM', 1),
                                                                                                   ('RD19', 'Đặng', 'Trà T', 'Nữ', '0910000019', '119 Đường Số 7, Hóc Môn, TP.HCM', 1),
                                                                                                   ('RD20', 'Võ', 'Đức U', 'Nam', '0911000020', '120 Đường Số 8, Củ Chi, TP.HCM', 1),
                                                                                                   ('RD21', 'Nguyễn', 'Thị V', 'Nữ', '0912000021', '121 Đường Số 9, Quận 1, TP.HCM', 1),
                                                                                                   ('RD22', 'Trần', 'Văn X', 'Nam', '0913000022', '122 Đường Số 10, Quận 2, TP.HCM', 1),
                                                                                                   ('RD23', 'Lê', 'Mỹ Y', 'Nữ', '0914000023', '123 Đường Số 11, Quận 3, TP.HCM', 0),
                                                                                                   ('RD24', 'Phạm', 'Gia Z', 'Nam', '0915000024', '124 Đường Số 12, Quận 4, TP.HCM', 1),
                                                                                                   ('RD25', 'Hoàng', 'Thị AA', 'Nữ', '0916000025', '125 Đường Số 1, Quận 5, TP.HCM', 1),
                                                                                                   ('RD26', 'Đỗ', 'Văn BB', 'Nam', '0917000026', '126 Đường Số 2, Quận 6, TP.HCM', 1),
                                                                                                   ('RD27', 'Võ', 'Ngọc CC', 'Nữ', '0918000027', '127 Đường Số 3, Quận 7, TP.HCM', 1),
                                                                                                   ('RD28', 'Huỳnh', 'Minh DD', 'Nam', '0919000028', '128 Đường Số 4, Quận 8, TP.HCM', 1),
                                                                                                   ('RD29', 'Nguyễn', 'Lan EE', 'Nữ', '0920000029', '129 Đường Số 5, Quận 9, TP.HCM', 1),
                                                                                                   ('RD30', 'Lê', 'Tuấn FF', 'Nam', '0921000030', '130 Đường Số 6, Quận 10, TP.HCM', 1);

-- Insert Authors
INSERT INTO `Author` (`firstName`, `lastName`, `isActive`) VALUES
                                                               ('Nguyễn', 'Nhật Ánh', 1),('Tô', 'Hoài', 1),('Nguyễn', 'Du', 1),('Xuân', 'Diệu', 1),('Hồ', 'Xuân Hương', 1),('Lưu', 'Quang Vũ', 1),('Nam', 'Cao', 1),('Victor', 'Hugo', 1),('J.K.', 'Rowling', 1),('George', 'Orwell', 1),('Jane', 'Austen', 1),('Ernest', 'Hemingway', 1),('Gabriel', 'García Márquez', 1),('Toni', 'Morrison', 1),('F. Scott', 'Fitzgerald', 1),('Haruki', 'Murakami', 1), ('Paulo', 'Coelho', 1), ('Agatha', 'Christie', 1),
                                                               ('Dale', 'Carnegie', 1), ('Yuval Noah', 'Harari', 1), ('Stephen', 'Covey', 1), ('Dan', 'Brown', 1), ('Arthur Conan', 'Doyle', 1),
                                                               ('Nguyễn', 'Ngọc Tư', 1), ('Trang', 'Hạ', 1), ('Rosie', 'Nguyễn', 1), ('Tony', 'Buổi Sáng', 1),
                                                               ('Harper', 'Lee', 1), ('Viktor', 'Frankl', 1), ('Antoine de Saint', 'Exupéry', 1), ('Adam', 'Khoo', 1), ('Eiichiro', 'Oda', 1), ('Nguyễn Trí', 'Đoàn', 1), ('James', 'Clear', 1);

-- Insert Categories
INSERT INTO `Category` (`name`, `isActive`) VALUES
                                                ('Tiểu thuyết', 1), ('Khoa học', 1), ('Thiếu nhi', 1), ('Tâm lý', 1), ('Kinh doanh', 1),('Lịch sử', 1), ('Trinh thám', 1), ('Khoa học viễn tưởng', 1), ('Tiểu sử', 1), ('Kỹ năng sống', 1), ('Văn học kinh điển', 1), ('Ngoại ngữ', 1), ('Truyện tranh', 1),
                                                ('Nấu ăn', 1), ('Du lịch', 1), ('Nghệ thuật', 1), ('Tôn giáo', 1), ('Sức khỏe', 1), ('Công nghệ thông tin', 1),
                                                ('Truyện ngắn', 1), ('Tản văn', 1), ('Thơ', 1), ('Giáo trình', 1);

-- Insert Publishers
INSERT INTO `Publisher` (`name`, `phone`, `address`, `isActive`) VALUES
                                                                     ('NXB Trẻ', '02839316289', '161 Lý Chính Thắng, Quận 3, TP.HCM', 1),('NXB Kim Đồng', '02439434730', '55 Quang Trung, Hai Bà Trưng, Hà Nội', 1),('NXB Giáo Dục VN', '02438220801', '81 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', 1),('NXB Văn Học', '0243852740', '18 Nguyễn Trường Tộ, Ba Đình, Hà Nội', 1),('Nhã Nam', '02437759017', '59 Đỗ Quang, Cầu Giấy, Hà Nội', 1), ('Đinh Tị Books', '02473093388', 'Trần Quốc Toản, Hoàn Kiếm, Hà Nội', 1), ('Alphabooks', '02437226234', '176 Thái Hà, Đống Đa, Hà Nội', 1),
                                                                     ('NXB Tổng hợp TP.HCM', '02838225340', '62 Nguyễn Thị Minh Khai, Quận 1, TP.HCM', 1),
                                                                     ('NXB Phụ Nữ Việt Nam', '02439717139', '39 Hàng Chuối, Hai Bà Trưng, Hà Nội', 1),
                                                                     ('Saigon Books', '02838207153', '97 Nguyễn Bỉnh Khiêm, Quận 1, TP.HCM', 1),
                                                                     ('NXB Hội Nhà Văn', '02438452080', '65 Nguyễn Du, Hai Bà Trưng, Hà Nội', 1),
                                                                     ('NXB Lao Động', '02439430260', '175 Giảng Võ, Đống Đa, Hà Nội', 1);

-- Insert Books (Số lượng ban đầu, sẽ được cập nhật bởi phiếu nhập hoàn thành)
INSERT INTO `Book` (`name`, `categoryId`, `authorId`, `publisherId`, `quantity`, `unitprice`, `yearOfpublication`) VALUES
                                                                                                                       ('Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 1, 1, 1, 30, 85000, 2015), -- quantity ban đầu = 30
                                                                                                                       ('Dế Mèn Phiêu Lưu Ký', 3, 2, 2, 40, 78000, 2012), -- quantity ban đầu = 40
                                                                                                                       ('Truyện Kiều', 11, 3, 4, 15, 95000, 2005), -- quantity ban đầu = 15
                                                                                                                       ('Harry Potter và Hòn Đá Phù Thủy', 1, 9, 1, 55, 145000, 2008), -- quantity ban đầu = 55
                                                                                                                       ('Nhà Giả Kim', 1, 17, 5, 40, 92000, 2016), -- quantity ban đầu = 40
                                                                                                                       ('Đắc Nhân Tâm', 10, 19, 7, 70, 120000, 2018), -- quantity ban đầu = 70
                                                                                                                       ('1984', 11, 10, 5, 45, 115000, 2017), -- quantity ban đầu = 45
                                                                                                                       ('Rừng Na Uy', 1, 16, 5, 55, 135000, 2010), -- quantity ban đầu = 55
                                                                                                                       ('Giết Con Chim Nhại', 11, 28, 4, 30, 105000, 1960), -- quantity ban đầu = 30
                                                                                                                       ('Án Mạng Trên Chuyến Tàu Tốc Hành Phương Đông', 7, 18, 1, 40, 99000, 2019), -- quantity ban đầu = 40
                                                                                                                       ('Sapiens: Lược Sử Loài Người', 2, 20, 7, 50, 180000, 2015), -- quantity ban đầu = 50
                                                                                                                       ('Tiếng Anh Giao Tiếp Cho Người Mất Gốc', 12, 1, 3, 75, 150000, 2022), -- Placeholder Author, quantity ban đầu = 75
                                                                                                                       ('One Piece - Tập 1', 13, 32, 2, 40, 25000, 2008), -- quantity ban đầu = 40
                                                                                                                       ('Để Con Được Ốm', 10, 33, 6, 60, 88000, 2016), -- quantity ban đầu = 60
                                                                                                                       ('Atomic Habits', 10, 34, 7, 50, 125000, 2021), -- quantity ban đầu = 50
                                                                                                                       ('7 Thói Quen Hiệu Quả', 10, 21, 8, 70, 130000, 2019), -- quantity ban đầu = 70
                                                                                                                       ('Mật Mã Da Vinci', 7, 22, 10, 60, 140000, 2006), -- quantity ban đầu = 60
                                                                                                                       ('Sherlock Holmes Toàn Tập - Tập 1', 7, 23, 4, 50, 190000, 2015), -- quantity ban đầu = 50
                                                                                                                       ('Bí Quyết Nấu Ăn Ngon Mỗi Ngày', 14, 1, 9, 40, 75000, 2020), -- Placeholder Author, quantity ban đầu = 40
                                                                                                                       ('Lập Trình Python Cơ Bản', 19, 1, 8, 55, 160000, 2023), -- Placeholder Author, quantity ban đầu = 55
                                                                                                                       ('Cánh Đồng Bất Tận', 20, 24, 1, 45, 82000, 2005), -- quantity ban đầu = 45
                                                                                                                       ('Tuổi Trẻ Đáng Giá Bao Nhiêu?', 10, 26, 11, 90, 89000, 2017), -- quantity ban đầu = 90
                                                                                                                       ('Trên Đường Băng', 21, 27, 1, 110, 109000, 2015), -- quantity ban đầu = 110
                                                                                                                       ('Số Đỏ', 11, 7, 4, 35, 70000, 2000), -- quantity ban đầu = 35
                                                                                                                       ('Homo Deus: Lược Sử Tương Lai', 2, 20, 7, 40, 185000, 2017), -- quantity ban đầu = 40
                                                                                                                       ('Đi Tìm Lẽ Sống', 4, 29, 8, 50, 95000, 2018), -- quantity ban đầu = 50
                                                                                                                       ('Hoàng Tử Bé', 3, 30, 6, 60, 65000, 2013), -- quantity ban đầu = 60
                                                                                                                       ('Tôi Tài Giỏi, Bạn Cũng Thế!', 10, 31, 9, 120, 110000, 2010), -- quantity ban đầu = 120
                                                                                                                       ('Những Người Khốn Khổ', 11, 8, 4, 25, 250000, 2012), -- quantity ban đầu = 25
                                                                                                                       ('Cafe Cùng Tony', 21, 27, 1, 100, 98000, 2014); -- quantity ban đầu = 100

-- Insert Suppliers
INSERT INTO `Supplier` (`id`, `name`, `phone`, `address`, `isActive`) VALUES
                                                                          ('SUP01', 'FAHASA', '0900636467', 'Nhiều chi nhánh', 1),
                                                                          ('SUP02', 'Phương Nam Book', '0190066569', 'Nhiều chi nhánh', 1),
                                                                          ('SUP03', 'Nhà Sách Cá Chép', '024394489', '211 Xã Đàn, Hà Nội', 1),
                                                                          ('SUP04', 'ADCBook', '0918006989', 'Nhiều chi nhánh', 1),
                                                                          ('SUP05', 'TiKi Trading', '0919006035', 'Online Platform', 1),
                                                                          ('SUP06', 'NhaSachPhuongDong', '0283838383', '12 Lê Lợi, Q1, TP.HCM', 1),
                                                                          ('SUP07', 'Vinabook.com', '0919006401', 'Online Platform', 1),
                                                                          ('SUP08', 'NXB Kim Đồng HCM', '0283939081', '248 Cống Quỳnh, Q1, TP.HCM', 1),
                                                                          ('SUP09', 'Công ty Sách Alpha', '0243722623', '176 Thái Hà, Hà Nội', 1);

-- ##############################################
-- ### GIAO DỊCH MƯỢN TRẢ (Phạt + Đã trả) ###
-- ##############################################
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES
                                                                                                                      (1, 'RD3', '2023-03-09 00:00:00', '2023-03-23 00:00:00', '2023-03-27 00:00:00', 'Phạt'), -- ID 1
                                                                                                                      (2, 'RD15', '2023-02-06 00:00:00', '2023-02-20 00:00:00', '2023-03-01 00:00:00', 'Phạt'), -- ID 2
                                                                                                                      (2, 'RD8', '2023-08-08 00:00:00', '2023-08-22 00:00:00', '2023-08-24 00:00:00', 'Phạt'), -- ID 3
                                                                                                                      (5, 'RD9', '2023-07-20 00:00:00', '2023-08-03 00:00:00', '2023-08-09 00:00:00', 'Phạt'), -- ID 4
                                                                                                                      (3, 'RD9', '2023-07-08 00:00:00', '2023-07-22 00:00:00', '2023-07-24 00:00:00', 'Phạt'), -- ID 5
                                                                                                                      (8, 'RD13', '2023-08-19 00:00:00', '2023-09-02 00:00:00', '2023-09-03 00:00:00', 'Phạt'), -- ID 6
                                                                                                                      (1, 'RD19', '2024-03-03 00:00:00', '2024-03-17 00:00:00', '2024-03-23 00:00:00', 'Phạt'), -- ID 7
                                                                                                                      (5, 'RD13', '2024-01-28 00:00:00', '2024-02-11 00:00:00', '2024-02-18 00:00:00', 'Phạt'), -- ID 8
                                                                                                                      (2, 'RD5', '2023-04-28 00:00:00', '2023-05-12 00:00:00', '2023-05-22 00:00:00', 'Phạt'), -- ID 9
                                                                                                                      (2, 'RD7', '2023-01-13 00:00:00', '2023-01-27 00:00:00', '2023-02-06 00:00:00', 'Phạt'), -- ID 10
                                                                                                                      (3, 'RD20', '2023-03-08 00:00:00', '2023-03-22 00:00:00', '2023-03-29 00:00:00', 'Phạt'), -- ID 11
                                                                                                                      (4, 'RD7', '2023-08-11 00:00:00', '2023-08-25 00:00:00', '2023-08-27 00:00:00', 'Phạt'), -- ID 12
                                                                                                                      (6, 'RD12', '2023-11-25 00:00:00', '2023-12-09 00:00:00', '2023-12-10 00:00:00', 'Phạt'), -- ID 13
                                                                                                                      (7, 'RD16', '2023-10-12 00:00:00', '2023-10-26 00:00:00', '2023-11-02 00:00:00', 'Phạt'), -- ID 14
                                                                                                                      (6, 'RD19', '2023-01-22 00:00:00', '2023-02-05 00:00:00', '2023-02-13 00:00:00', 'Phạt'), -- ID 15
                                                                                                                      (1, 'RD11', '2023-10-19 00:00:00', '2023-11-02 00:00:00', '2023-11-06 00:00:00', 'Phạt'), -- ID 16
                                                                                                                      (1, 'RD12', '2023-03-28 00:00:00', '2023-04-11 00:00:00', '2023-04-16 00:00:00', 'Phạt'), -- ID 17
                                                                                                                      (3, 'RD19', '2024-01-21 00:00:00', '2024-02-04 00:00:00', '2024-02-11 00:00:00', 'Phạt'), -- ID 18
                                                                                                                      (4, 'RD11', '2023-12-15 00:00:00', '2023-12-29 00:00:00', '2024-01-02 00:00:00', 'Phạt'), -- ID 19
                                                                                                                      (8, 'RD5', '2023-04-04 00:00:00', '2023-04-18 00:00:00', '2023-04-23 00:00:00', 'Phạt'), -- ID 20
                                                                                                                      (3, 'RD17', '2023-03-19 00:00:00', '2023-04-02 00:00:00', '2023-04-05 00:00:00', 'Phạt'), -- ID 21
                                                                                                                      (3, 'RD3', '2023-12-11 00:00:00', '2023-12-25 00:00:00', '2024-01-02 00:00:00', 'Phạt'), -- ID 22
                                                                                                                      (6, 'RD9', '2023-01-11 00:00:00', '2023-01-25 00:00:00', '2023-01-26 00:00:00', 'Phạt'), -- ID 23
                                                                                                                      (2, 'RD19', '2024-02-16 00:00:00', '2024-03-01 00:00:00', '2024-03-04 00:00:00', 'Phạt'), -- ID 24
                                                                                                                      (9, 'RD7', '2023-01-20 00:00:00', '2023-02-03 00:00:00', '2023-02-09 00:00:00', 'Phạt'), -- ID 25
                                                                                                                      (5, 'RD1', '2023-02-22 00:00:00', '2023-03-08 00:00:00', '2023-03-10 00:00:00', 'Phạt'), -- ID 26
                                                                                                                      (5, 'RD8', '2023-07-06 00:00:00', '2023-07-20 00:00:00', '2023-07-26 00:00:00', 'Phạt'), -- ID 27
                                                                                                                      (4, 'RD7', '2023-09-20 00:00:00', '2023-10-04 00:00:00', '2023-10-09 00:00:00', 'Phạt'), -- ID 28
                                                                                                                      (10, 'RD8', '2023-11-20 00:00:00', '2023-12-04 00:00:00', '2023-12-13 00:00:00', 'Phạt'), -- ID 29
                                                                                                                      (2, 'RD5', '2023-03-04 00:00:00', '2023-03-18 00:00:00', '2023-03-24 00:00:00', 'Phạt'), -- ID 30
                                                                                                                      (7, 'RD10', '2023-02-02 00:00:00', '2023-02-16 00:00:00', '2023-02-15 00:00:00', 'Đã_Trả'), -- ID 31
                                                                                                                      (1, 'RD5', '2023-08-01 00:00:00', '2023-08-15 00:00:00', '2023-08-14 00:00:00', 'Đã_Trả'), -- ID 32
                                                                                                                      (8, 'RD4', '2024-03-09 00:00:00', '2024-03-23 00:00:00', '2024-03-23 00:00:00', 'Đã_Trả'), -- ID 33
                                                                                                                      (5, 'RD15', '2023-07-30 00:00:00', '2023-08-13 00:00:00', '2023-08-13 00:00:00', 'Đã_Trả'), -- ID 34
                                                                                                                      (3, 'RD8', '2023-07-10 00:00:00', '2023-07-24 00:00:00', '2023-07-22 00:00:00', 'Đã_Trả'), -- ID 35
                                                                                                                      (9, 'RD13', '2024-02-07 00:00:00', '2024-02-21 00:00:00', '2024-02-19 00:00:00', 'Đã_Trả'), -- ID 36
                                                                                                                      (2, 'RD17', '2023-11-03 00:00:00', '2023-11-17 00:00:00', '2023-11-17 00:00:00', 'Đã_Trả'), -- ID 37
                                                                                                                      (8, 'RD6', '2023-07-26 00:00:00', '2023-08-09 00:00:00', '2023-08-08 00:00:00', 'Đã_Trả'), -- ID 38
                                                                                                                      (3, 'RD4', '2023-06-08 00:00:00', '2023-06-22 00:00:00', '2023-06-22 00:00:00', 'Đã_Trả'), -- ID 39
                                                                                                                      (5, 'RD1', '2023-01-14 00:00:00', '2023-01-28 00:00:00', '2023-01-27 00:00:00', 'Đã_Trả'); -- ID 40

INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES
                                                                                                         (28, 1, 1, 'Quá_Hạn', '2023-03-27 00:00:00'),
                                                                                                         (21, 2, 1, 'Quá_Hạn', '2023-03-01 00:00:00'),
                                                                                                         (23, 3, 1, 'Quá_Hạn', '2023-08-24 00:00:00'),
                                                                                                         (26, 4, 1, 'Quá_Hạn', '2023-08-09 00:00:00'),
                                                                                                         (19, 5, 1, 'Quá_Hạn', '2023-07-24 00:00:00'),
                                                                                                         (17, 6, 1, 'Quá_Hạn', '2023-09-03 00:00:00'),
                                                                                                         (11, 7, 1, 'Quá_Hạn', '2024-03-23 00:00:00'),
                                                                                                         (30, 8, 1, 'Quá_Hạn', '2024-02-18 00:00:00'),
                                                                                                         (16, 9, 1, 'Quá_Hạn', '2023-05-22 00:00:00'),
                                                                                                         (11, 10, 1, 'Quá_Hạn', '2023-02-06 00:00:00'),
                                                                                                         (9, 11, 1, 'Quá_Hạn', '2023-03-29 00:00:00'),
                                                                                                         (3, 12, 1, 'Quá_Hạn', '2023-08-27 00:00:00'),
                                                                                                         (20, 13, 1, 'Quá_Hạn', '2023-12-10 00:00:00'),
                                                                                                         (28, 14, 1, 'Quá_Hạn', '2023-11-02 00:00:00'),
                                                                                                         (11, 15, 1, 'Quá_Hạn', '2023-02-13 00:00:00'),
                                                                                                         (7, 16, 1, 'Quá_Hạn', '2023-11-06 00:00:00'),
                                                                                                         (27, 17, 1, 'Quá_Hạn', '2023-04-16 00:00:00'),
                                                                                                         (15, 18, 1, 'Quá_Hạn', '2024-02-11 00:00:00'),
                                                                                                         (22, 19, 1, 'Quá_Hạn', '2024-01-02 00:00:00'),
                                                                                                         (14, 20, 1, 'Quá_Hạn', '2023-04-23 00:00:00'),
                                                                                                         (14, 21, 1, 'Quá_Hạn', '2023-04-05 00:00:00'),
                                                                                                         (14, 22, 1, 'Quá_Hạn', '2024-01-02 00:00:00'),
                                                                                                         (4, 23, 1, 'Quá_Hạn', '2023-01-26 00:00:00'),
                                                                                                         (18, 24, 1, 'Quá_Hạn', '2024-03-04 00:00:00'),
                                                                                                         (4, 25, 1, 'Quá_Hạn', '2023-02-09 00:00:00'),
                                                                                                         (14, 26, 1, 'Quá_Hạn', '2023-03-10 00:00:00'),
                                                                                                         (13, 27, 1, 'Quá_Hạn', '2023-07-26 00:00:00'),
                                                                                                         (7, 28, 1, 'Quá_Hạn', '2023-10-09 00:00:00'),
                                                                                                         (22, 29, 1, 'Quá_Hạn', '2023-12-13 00:00:00'),
                                                                                                         (19, 30, 1, 'Quá_Hạn', '2023-03-24 00:00:00'),
                                                                                                         (23, 31, 1, 'Đã_Trả', '2023-02-15 00:00:00'),
                                                                                                         (3, 32, 1, 'Đã_Trả', '2023-08-14 00:00:00'),
                                                                                                         (13, 33, 1, 'Đã_Trả', '2024-03-23 00:00:00'),
                                                                                                         (1, 34, 1, 'Đã_Trả', '2023-08-13 00:00:00'),
                                                                                                         (21, 35, 1, 'Đã_Trả', '2023-07-22 00:00:00'),
                                                                                                         (10, 36, 1, 'Đã_Trả', '2024-02-19 00:00:00'),
                                                                                                         (8, 37, 1, 'Đã_Trả', '2023-11-17 00:00:00'),
                                                                                                         (2, 38, 1, 'Đã_Trả', '2023-08-08 00:00:00'),
                                                                                                         (21, 39, 1, 'Đã_Trả', '2023-06-22 00:00:00'),
                                                                                                         (29, 40, 1, 'Đã_Trả', '2023-01-27 00:00:00');

-- ##############################################
-- ### GIAO DỊCH PHẠT ###
-- ##############################################
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES
                                                                                                  ('PP1', '2023-03-27 00:00:00', 40000.00, 'Đã_Thanh_Toán', 6, '2023-03-28 00:00:00'),
                                                                                                  ('PP2', '2023-03-01 00:00:00', 25000.00, 'Đã_Thanh_Toán', 5, '2023-03-02 00:00:00'),
                                                                                                  ('PP3', '2023-08-24 00:00:00', 25000.00, 'Đã_Thanh_Toán', 3, '2023-08-25 00:00:00'),
                                                                                                  ('PP4', '2023-08-09 00:00:00', 40000.00, 'Đã_Thanh_Toán', 4, '2023-08-10 00:00:00'),
                                                                                                  ('PP5', '2023-07-24 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP6', '2023-09-03 00:00:00', 40000.00, 'Đã_Thanh_Toán', 2, '2023-09-04 00:00:00'),
                                                                                                  ('PP7', '2024-03-23 00:00:00', 25000.00, 'Đã_Thanh_Toán', 3, '2024-03-24 00:00:00'),
                                                                                                  ('PP8', '2024-02-18 00:00:00', 35000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP9', '2023-05-22 00:00:00', 20000.00, 'Đã_Thanh_Toán', 7, '2023-05-23 00:00:00'),
                                                                                                  ('PP10', '2023-02-06 00:00:00', 40000.00, 'Đã_Thanh_Toán', 9, '2023-02-07 00:00:00'),
                                                                                                  ('PP11', '2023-03-29 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP12', '2023-08-27 00:00:00', 15000.00, 'Đã_Thanh_Toán', 2, '2023-08-28 00:00:00'),
                                                                                                  ('PP13', '2023-12-10 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP14', '2023-11-02 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP15', '2023-02-13 00:00:00', 35000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP16', '2023-11-06 00:00:00', 20000.00, 'Đã_Thanh_Toán', 7, '2023-11-07 00:00:00'),
                                                                                                  ('PP17', '2023-04-16 00:00:00', 35000.00, 'Đã_Thanh_Toán', 10, '2023-04-17 00:00:00'),
                                                                                                  ('PP18', '2024-02-11 00:00:00', 15000.00, 'Đã_Thanh_Toán', 2, '2024-02-12 00:00:00'),
                                                                                                  ('PP19', '2024-01-02 00:00:00', 15000.00, 'Đã_Thanh_Toán', 6, '2024-01-03 00:00:00'),
                                                                                                  ('PP20', '2023-04-23 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP21', '2023-04-05 00:00:00', 30000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP22', '2024-01-02 00:00:00', 15000.00, 'Đã_Thanh_Toán', 10, '2024-01-03 00:00:00'),
                                                                                                  ('PP23', '2023-01-26 00:00:00', 25000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP24', '2024-03-04 00:00:00', 15000.00, 'Đã_Thanh_Toán', 1, '2024-03-05 00:00:00'),
                                                                                                  ('PP25', '2023-02-09 00:00:00', 20000.00, 'Đã_Thanh_Toán', 1, '2023-02-10 00:00:00'),
                                                                                                  ('PP26', '2023-03-10 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP27', '2023-07-26 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP28', '2023-10-09 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP29', '2023-12-13 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL),
                                                                                                  ('PP30', '2023-03-24 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);

INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES
                                                                                                          ('PP1', 28, 1, 'Hư sách', 40000.00, 1),
                                                                                                          ('PP2', 21, 2, 'Hư sách', 25000.00, 1),
                                                                                                          ('PP3', 23, 3, 'Hư sách', 25000.00, 1),
                                                                                                          ('PP4', 26, 4, 'Trả sách trễ 6 ngày', 40000.00, 1),
                                                                                                          ('PP5', 19, 5, 'Mất sách', 40000.00, 1),
                                                                                                          ('PP6', 17, 6, 'Hư sách', 40000.00, 1),
                                                                                                          ('PP7', 11, 7, 'Mất sách', 25000.00, 1),
                                                                                                          ('PP8', 30, 8, 'Mất sách', 35000.00, 1),
                                                                                                          ('PP9', 16, 9, 'Hư sách', 20000.00, 1),
                                                                                                          ('PP10', 11, 10, 'Hư sách', 40000.00, 1),
                                                                                                          ('PP11', 9, 11, 'Mất sách', 20000.00, 1),
                                                                                                          ('PP12', 3, 12, 'Hư sách', 15000.00, 1),
                                                                                                          ('PP13', 20, 13, 'Mất sách', 20000.00, 1),
                                                                                                          ('PP14', 28, 14, 'Mất sách', 40000.00, 1),
                                                                                                          ('PP15', 11, 15, 'Hư sách', 35000.00, 1),
                                                                                                          ('PP16', 7, 16, 'Trả sách trễ 4 ngày', 20000.00, 1),
                                                                                                          ('PP17', 27, 17, 'Trả sách trễ 5 ngày', 35000.00, 1),
                                                                                                          ('PP18', 15, 18, 'Hư sách', 15000.00, 1),
                                                                                                          ('PP19', 22, 19, 'Hư sách', 15000.00, 1),
                                                                                                          ('PP20', 14, 20, 'Trả sách trễ 5 ngày', 15000.00, 1),
                                                                                                          ('PP21', 14, 21, 'Mất sách', 30000.00, 1),
                                                                                                          ('PP22', 14, 22, 'Mất sách', 15000.00, 1),
                                                                                                          ('PP23', 4, 23, 'Trả sách trễ 1 ngày', 25000.00, 1),
                                                                                                          ('PP24', 18, 24, 'Trả sách trễ 3 ngày', 15000.00, 1),
                                                                                                          ('PP25', 4, 25, 'Mất sách', 20000.00, 1),
                                                                                                          ('PP26', 14, 26, 'Trả sách trễ 2 ngày', 15000.00, 1),
                                                                                                          ('PP27', 13, 27, 'Mất sách', 40000.00, 1),
                                                                                                          ('PP28', 7, 28, 'Trả sách trễ 5 ngày', 15000.00, 1),
                                                                                                          ('PP29', 22, 29, 'Hư sách', 20000.00, 1),
                                                                                                          ('PP30', 19, 30, 'Mất sách', 20000.00, 1);


-- ##############################################################
-- ### BỔ SUNG PHIẾU MƯỢN TRẠNG THÁI 'Đang_Mượn' (Năm 2025) ###
-- ##############################################################

-- Giả sử hôm nay là khoảng đầu tháng 5 năm 2025
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES
                                                                                                                      (4, 'RD2', '2025-04-10 09:30:00', '2025-04-24 09:30:00', NULL, 'Đang_Mượn'), -- ID 41
                                                                                                                      (6, 'RD18', '2025-04-12 14:00:00', '2025-04-26 14:00:00', NULL, 'Đang_Mượn'), -- ID 42
                                                                                                                      (7, 'RD25', '2025-04-15 10:15:00', '2025-04-29 10:15:00', NULL, 'Đang_Mượn'), -- ID 43
                                                                                                                      (1, 'RD1', '2025-04-18 11:00:00', '2025-05-02 11:00:00', NULL, 'Đang_Mượn'), -- ID 44
                                                                                                                      (8, 'RD30', '2025-04-20 16:30:00', '2025-05-04 16:30:00', NULL, 'Đang_Mượn'), -- ID 45
                                                                                                                      (2, 'RD14', '2025-04-22 08:45:00', '2025-05-06 08:45:00', NULL, 'Đang_Mượn'), -- ID 46
                                                                                                                      (5, 'RD22', '2025-04-25 13:00:00', '2025-05-09 13:00:00', NULL, 'Đang_Mượn'), -- ID 47
                                                                                                                      (10, 'RD16', '2025-04-28 15:00:00', '2025-05-12 15:00:00', NULL, 'Đang_Mượn'), -- ID 48
                                                                                                                      (3, 'RD29', '2025-04-30 09:00:00', '2025-05-14 09:00:00', NULL, 'Đang_Mượn'), -- ID 49
                                                                                                                      (9, 'RD11', '2025-05-02 10:30:00', '2025-05-16 10:30:00', NULL, 'Đang_Mượn'), -- ID 50
                                                                                                                      (1, 'RD8', '2025-05-03 14:45:00', '2025-05-17 14:45:00', NULL, 'Đang_Mượn'), -- ID 51
                                                                                                                      (4, 'RD5', '2025-05-04 11:20:00', '2025-05-18 11:20:00', NULL, 'Đang_Mượn'); -- ID 52

-- Thêm chi tiết cho các phiếu mượn 'Đang_Mượn' (borrowSheetId từ 41 đến 52)
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES
                                                                                                         (5, 41, 1, 'Đang_Mượn', NULL),   -- Nhà Giả Kim
                                                                                                         (16, 41, 1, 'Đang_Mượn', NULL),  -- Rừng Na Uy
                                                                                                         (15, 42, 1, 'Đang_Mượn', NULL),  -- Atomic Habits
                                                                                                         (24, 43, 1, 'Đang_Mượn', NULL),  -- Số Đỏ
                                                                                                         (1, 44, 1, 'Đang_Mượn', NULL),   -- Tôi Thấy Hoa Vàng...
                                                                                                         (13, 45, 1, 'Đang_Mượn', NULL),  -- One Piece Tập 1
                                                                                                         (26, 46, 1, 'Đang_Mượn', NULL),  -- Đi Tìm Lẽ Sống
                                                                                                         (6, 47, 1, 'Đang_Mượn', NULL),   -- Đắc Nhân Tâm
                                                                                                         (10, 48, 1, 'Đang_Mượn', NULL),  -- Án Mạng Trên Chuyến Tàu...
                                                                                                         (22, 49, 1, 'Đang_Mượn', NULL),  -- Tuổi Trẻ Đáng Giá Bao Nhiêu?
                                                                                                         (12, 50, 1, 'Đang_Mượn', NULL),  -- Tiếng Anh Giao Tiếp...
                                                                                                         (30, 51, 1, 'Đang_Mượn', NULL),  -- Cafe Cùng Tony
                                                                                                         (4, 52, 1, 'Đang_Mượn', NULL),   -- Harry Potter...
                                                                                                         (29, 52, 1, 'Đang_Mượn', NULL);  -- Những Người Khốn Khổ

-- ##########################################################################
-- ### BỔ SUNG PHIẾU NHẬP VỚI CÁC TRẠNG THÁI VÀ PHÂN BỔ THEO NĂM ###
-- ##########################################################################

-- Phiếu nhập trạng thái 'Hoàn_Thành' (Năm 2023, 2024) - ID bắt đầu từ 1
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES
                                                                                                  ('SUP01', 2, 0, '2023-05-15 10:00:00', 'Hoàn_Thành'), -- ID 1
                                                                                                  ('SUP08', 4, 0, '2023-11-20 14:30:00', 'Hoàn_Thành'), -- ID 2
                                                                                                  ('SUP05', 1, 0, '2024-03-10 09:00:00', 'Hoàn_Thành'), -- ID 3
                                                                                                  ('SUP02', 7, 0, '2024-09-05 11:15:00', 'Hoàn_Thành'); -- ID 4

-- Chi tiết cho phiếu nhập 'Hoàn_Thành'
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES
                                                                                                          (1, 1, 20, 75000, 1500000),
                                                                                                          (1, 6, 30, 110000, 3300000),
                                                                                                          (2, 2, 25, 70000, 1750000),
                                                                                                          (2, 13, 50, 22000, 1100000),
                                                                                                          (3, 5, 40, 85000, 3400000),
                                                                                                          (3, 15, 35, 115000, 4025000),
                                                                                                          (4, 4, 15, 130000, 1950000),
                                                                                                          (4, 10, 20, 90000, 1800000),
                                                                                                          (4, 28, 10, 100000, 1000000);

-- Cập nhật totalAmount cho phiếu nhập 'Hoàn_Thành'
UPDATE PurchaseOrders SET totalAmount = (SELECT SUM(SubTotal) FROM PurchaseOrderDetails WHERE purchaseOrderId = 1) WHERE id = 1;
UPDATE PurchaseOrders SET totalAmount = (SELECT SUM(SubTotal) FROM PurchaseOrderDetails WHERE purchaseOrderId = 2) WHERE id = 2;
UPDATE PurchaseOrders SET totalAmount = (SELECT SUM(SubTotal) FROM PurchaseOrderDetails WHERE purchaseOrderId = 3) WHERE id = 3;
UPDATE PurchaseOrders SET totalAmount = (SELECT SUM(SubTotal) FROM PurchaseOrderDetails WHERE purchaseOrderId = 4) WHERE id = 4;

-- Phiếu nhập trạng thái 'Đã_Hủy' (Năm 2023, 2024) - ID bắt đầu từ 5
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES
                                                                                                  ('SUP03', 5, 0, '2023-08-01 16:00:00', 'Đã_Hủy'), -- ID 5
                                                                                                  ('SUP07', 3, 0, '2024-01-25 10:45:00', 'Đã_Hủy'), -- ID 6
                                                                                                  ('SUP04', 9, 0, '2024-07-18 13:20:00', 'Đã_Hủy'); -- ID 7

-- Chi tiết cho phiếu nhập 'Đã_Hủy'
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES
                                                                                                          (5, 7, 10, 110000, 1100000),
                                                                                                          (5, 11, 5, 170000, 850000),
                                                                                                          (6, 16, 15, 125000, 1875000),
                                                                                                          (7, 21, 20, 80000, 1600000);

-- Phiếu nhập trạng thái 'Đang_Chờ' (Năm 2025) - ID bắt đầu từ 8
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES
                                                                                                  ('SUP09', 6, 0, '2025-04-20 08:00:00', 'Đang_Chờ'), -- ID 8
                                                                                                  ('SUP06', 10, 0, '2025-04-29 15:00:00', 'Đang_Chờ'), -- ID 9
                                                                                                  ('SUP01', 2, 0, '2025-05-03 10:00:00', 'Đang_Chờ'); -- ID 10

-- Chi tiết cho phiếu nhập 'Đang_Chờ'
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES
                                                                                                          (8, 18, 25, 180000, 4500000),
                                                                                                          (8, 25, 10, 180000, 1800000),
                                                                                                          (9, 22, 50, 85000, 4250000),
                                                                                                          (9, 30, 40, 95000, 3800000),
                                                                                                          (10, 3, 15, 90000, 1350000),
                                                                                                          (10, 20, 30, 150000, 4500000);


-- ##############################################
-- ### CẬP NHẬT SỐ LƯỢNG SAU KHI INSERT DATA ###
-- ##############################################

-- Bước 1: Cập nhật số lượng sách đang được mượn (`borrowedQuantity`)
-- Tính toán lại dựa trên TOÀN BỘ dữ liệu BorrowDetails có substatus = 'Đang_Mượn'
UPDATE Book b
    LEFT JOIN (
    SELECT
    bd.bookId,
    SUM(bd.quantity) AS currently_borrowed_count
    FROM
    BorrowDetails bd
    WHERE
    bd.substatus = 'Đang_Mượn'
    GROUP BY
    bd.bookId
    ) AS borrowed_counts ON b.id = borrowed_counts.bookId
    SET
        b.borrowedQuantity = COALESCE(borrowed_counts.currently_borrowed_count, 0);

-- Bước 2: Cập nhật tổng số lượng sách trong kho (`quantity`) từ các phiếu nhập đã hoàn thành
-- Cộng số lượng từ các phiếu nhập 'Hoàn_Thành' vào số lượng ban đầu đã INSERT
UPDATE Book b
    INNER JOIN PurchaseOrderDetails pod ON b.id = pod.bookId
    INNER JOIN PurchaseOrders po ON pod.purchaseOrderId = po.id
    SET
        b.quantity = b.quantity + pod.quantity -- Cộng số lượng nhập vào kho
WHERE
    po.status = 'Hoàn_Thành';

-- Bước 3: Cập nhật số lượng đầu sách cho mỗi tác giả dựa trên bảng Book
UPDATE Author a
    JOIN (
    SELECT
    authorId,
    COUNT(id) AS book_count
    FROM
    Book
    GROUP BY
    authorId
    ) AS book_counts ON a.id = book_counts.authorId
    SET
        a.quantity = book_counts.book_count;

-- Bước 4: Cập nhật số lượng về 0 cho những tác giả không có sách nào trong bảng Book
UPDATE Author
SET quantity = 0
WHERE id NOT IN (SELECT DISTINCT authorId FROM Book);

-- Bước 5: Xóa cột complianceCount khỏi bảng Reader (Nếu tồn tại)
-- Kiểm tra xem cột có tồn tại không trước khi xóa để tránh lỗi nếu script chạy lại
SET @exist := (SELECT COUNT(*) FROM information_schema.columns
               WHERE table_schema = DATABASE() AND table_name = 'Reader' AND column_name = 'complianceCount');
SET @sql := IF(@exist > 0, 'ALTER TABLE `Reader` DROP COLUMN `complianceCount`', 'SELECT "Column complianceCount does not exist."');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Hiển thị số lượng cuối cùng để kiểm tra (Tùy chọn)
SELECT id, name, quantity, borrowedQuantity FROM Book ORDER BY id;
SELECT id, firstName, lastName, quantity FROM Author ORDER BY id;