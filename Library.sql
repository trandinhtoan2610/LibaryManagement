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
                          `complianceCount` INT NOT NULL DEFAULT 3,
                          `isActive` SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE `Author` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `quantity` INT NOT NULL DEFAULT 0, -- Cột này có vẻ thừa vì số lượng sách của tác giả nên được tính từ bảng Book
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
                        `quantity` INT NOT NULL, -- Số lượng tổng trong kho (sẽ được cập nhật từ phiếu nhập)
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
                                   `borrowedDate` DATETIME NOT NULL, -- Không dùng DEFAULT CURRENT_TIMESTAMP nữa để nhập dữ liệu quá khứ
                                   `duedate` DATETIME NOT NULL,
                                   `actualReturnDate` DATETIME DEFAULT NULL,
                                   `status` ENUM('Đang_Mượn', 'Đã_Trả', 'Phạt') NOT NULL DEFAULT 'Đang_Mượn'
);

CREATE TABLE `BorrowDetails` (
                                 `bookId` BIGINT NOT NULL,
                                 `borrowSheetId` BIGINT NOT NULL,
                                 `quantity` INT NOT NULL DEFAULT 1, -- Thường là 1 cho mỗi cuốn sách
                                 `substatus` ENUM('Đang_Mượn', 'Đã_Trả', 'Mất_Sách', 'Hư_Sách', 'Quá_Hạn') NOT NULL DEFAULT 'Đang_Mượn',
                                 `actualReturnDate` DATETIME DEFAULT NULL, -- Đồng bộ với Borrow_in_Sheet khi trả/phạt
                                 PRIMARY KEY (`bookId`, `borrowSheetId`)
);

CREATE TABLE `PurchaseOrders` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `supplierId` VARCHAR(10) NOT NULL,
                                  `employeeId` BIGINT NOT NULL,
                                  `totalAmount` DECIMAL(12,2) NOT NULL DEFAULT 0, -- Tăng độ lớn cho tổng tiền
                                  `buyDate` DATETIME NOT NULL, -- Không dùng DEFAULT CURRENT_TIMESTAMP
                                  `status` ENUM('Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy') NOT NULL DEFAULT 'Đang_Chờ'
);

CREATE TABLE `PurchaseOrderDetails` (
                                        `purchaseOrderId` BIGINT NOT NULL,
                                        `bookId` BIGINT NOT NULL,
                                        `quantity` INT NOT NULL DEFAULT 1,
                                        `unitPrice` DECIMAL(10,2) NOT NULL,
                                        `SubTotal` DECIMAL(12,2) NOT NULL, -- Tăng độ lớn
                                        PRIMARY KEY (`purchaseOrderId`, `bookId`)
);

-- ============================================================
-- REVERTED PENALTY TABLES BASED ON sql_penalty_tables_revert
-- ============================================================
-- Create the Penalty table (as per artifact)
CREATE TABLE `Penalty` (
                           `id` VARCHAR(10) PRIMARY KEY,
                           `penaltyDate` DATETIME NOT NULL,
                           `totalamount` DECIMAL(10,2) NOT NULL,
                           `status` ENUM ('Đã_Thanh_Toán', 'Chưa_Thanh_Toán') NOT NULL DEFAULT 'Chưa_Thanh_Toán',
                           `employeeID` BIGINT, -- ID of the employee who processed the payment (can be NULL if not paid)
                           `payDate` DATETIME DEFAULT NULL, -- Date the penalty was paid
                           FOREIGN KEY (`employeeID`) REFERENCES `Employee`(`id`) ON DELETE SET NULL ON UPDATE CASCADE -- If employee is deleted, set FK to NULL
);

-- Create the PenaltyDetails table (as per artifact)
CREATE TABLE `PenaltyDetails` (
                                  `penaltyId` VARCHAR(10) NOT NULL, -- Foreign key to Penalty table
                                  `bookId` BIGINT NOT NULL, -- ID of the specific book related to the penalty
                                  `borrowId` BIGINT NOT NULL, -- ID of the borrow sheet this penalty detail relates to
                                  `name` VARCHAR(255), -- Name of the reason/type of penalty (e.g., "Trả muộn", "Mất sách"). Consider making this an ENUM or linking to a separate reason table.
                                  `subamount` DECIMAL(10,2) NOT NULL, -- The penalty amount for this specific book/reason within the overall penalty
                                  `bookQuantity` INT NOT NULL, -- The number of copies of the book involved in this penalty detail (usually 1)
                                  PRIMARY KEY (`penaltyId`, `bookId`, `borrowId`), -- Composite primary key
                                  FOREIGN KEY (`penaltyId`) REFERENCES `Penalty`(`id`) ON DELETE CASCADE ON UPDATE CASCADE -- If the main penalty record is deleted, delete the details
    -- Note: No direct FK to Book or BorrowDetails here as per the request. Ensure data integrity through application logic or triggers if needed.
);
-- ============================================================

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

-- Foreign key for Penalty.employeeID is already in CREATE TABLE.
-- No FK for Penalty.readerId as it's not in the reverted table structure.
-- No FK for PenaltyDetails to BorrowDetails as per artifact and previous script state.


-- #####################################################################
-- ### INSERT SAMPLE DATA - MORE REALISTIC DATES, TIMES, QUANTITIES ###
-- #####################################################################

-- Insert Roles (Chỉ giữ lại 3 vai trò đầu tiên)
INSERT INTO `Role` (`name`) VALUES
                                ('Admin'),
                                ('Staff'),
                                ('Employee');

-- Insert Employees (Thêm nhiều nhân viên hơn với 3 vai trò trên)
INSERT INTO `Employee` (`firstName`, `lastName`, `gender`, `username`, `password`, `roleId`, `phone`, `address`, `salary`, `isActive`) VALUES
-- Những nhân viên ban đầu
('Nguyễn', 'Văn Thành', 'Nam', 'thanhnv_admin', 'hashed_pw', 1, '0912345670', '123 Đường ABC, Quận 1, TP.HCM', 15000000, 1), -- Admin
('Trần', 'Thị Lan', 'Nữ', 'lantt_staff', 'hashed_pw', 2, '0987654321', '456 Đường XYZ, Quận 3, TP.HCM', 9000000, 1),  -- Staff
('Lê', 'Hoàng Minh', 'Nam', 'minhlh_emp', 'hashed_pw', 3, '0912345678', '789 Đường KLM, Quận Bình Thạnh, TP.HCM', 7500000, 1), -- Employee
('Phạm', 'Hoài An', 'Nữ', 'anph_staff', 'hashed_pw', 2, '0911223344', '101 Đường DEF, Quận Gò Vấp, TP.HCM', 9500000, 1),  -- Staff
('Vũ', 'Tuấn Khải', 'Nam', 'khaivt_emp', 'hashed_pw', 3, '0905678910', '202 Đường GHI, Quận Tân Bình, TP.HCM', 8000000, 1),  -- Employee

-- Thêm nhân viên mới
('Chí', 'Văn', 'Nam', 'van', 'van', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1), -- Staff
('Võ Duy', 'Thanh', 'Nam', 'thanh', 'thanh', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1), -- Staff
('Nhan Chí', 'Phong', 'Nam', 'giaitich1', 'giaitich2', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1), -- Staff
('Hoàng', 'Quý', 'Nam', 'hoang', 'hoang', 1, '0909112233', '303 Đường JQK, Quận Phú Nhuận, TP.HCM', 0, 1), -- Staff
('Hoàng', 'Gia Huy', 'Nam', 'huyhg_staff', 'hashed_pw', 2, '0909112233', '303 Đường JKL, Quận Phú Nhuận, TP.HCM', 8800000, 1), -- Staff
('Đặng', 'Mỹ Linh', 'Nữ', 'linhdm_emp', 'hashed_pw', 3, '0977889900', '404 Đường MNO, Quận 10, TP.HCM', 7200000, 1),   -- Employee
('Bùi', 'Quốc Bảo', 'Nam', 'baobq_emp', 'hashed_pw', 3, '0933445566', '505 Đường PQR, Quận Tân Phú, TP.HCM', 7800000, 1),   -- Employee
('Ngô', 'Phương Thảo', 'Nữ', 'thaonp_staff', 'hashed_pw', 2, '0944556677', '606 Đường STU, Quận 7, TP.HCM', 9200000, 1),  -- Staff
('Lý', 'Chí Thanh', 'Nam', 'thanhlt_admin', 'hashed_pw', 1, '0966778899', '707 Đường VWX, Quận 2, TP.HCM', 16000000, 1), -- Admin (thêm admin thứ 2)
('Mai', 'Thu Trang', 'Nữ', 'trangmt_emp', 'hashed_pw', 3, '0922334455', '808 Đường YZA, Quận 12, TP.HCM', 7000000, 1),   -- Employee
('Dương', 'Minh Đức', 'Nam', 'ducdm_staff', 'hashed_pw', 2, '0955667788', '909 Đường BCD, Huyện Bình Chánh, TP.HCM', 8500000, 0), -- Staff (isActive = 0)
('Trịnh', 'Ngọc Mai', 'Nữ', 'maitn_emp', 'hashed_pw', 3, '0918765432', '111 Đường EFG, Quận 5, TP.HCM', 7300000, 1),   -- Employee
('Phan', 'Đăng Khoa', 'Nam', 'khoapd_staff', 'hashed_pw', 2, '0981234567', '222 Đường HIJ, Quận 6, TP.HCM', 9100000, 1);  -- Staff

-- Insert Readers (Thêm nhiều độc giả hơn)
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
                                                                                                   ('RD23', 'Lê', 'Mỹ Y', 'Nữ', '0914000023', '123 Đường Số 11, Quận 3, TP.HCM', 0), -- Reader không hoạt động
                                                                                                   ('RD24', 'Phạm', 'Gia Z', 'Nam', '0915000024', '124 Đường Số 12, Quận 4, TP.HCM', 1),
                                                                                                   ('RD25', 'Hoàng', 'Thị AA', 'Nữ', '0916000025', '125 Đường Số 1, Quận 5, TP.HCM', 1),
                                                                                                   ('RD26', 'Đỗ', 'Văn BB', 'Nam', '0917000026', '126 Đường Số 2, Quận 6, TP.HCM', 1),
                                                                                                   ('RD27', 'Võ', 'Ngọc CC', 'Nữ', '0918000027', '127 Đường Số 3, Quận 7, TP.HCM', 1),
                                                                                                   ('RD28', 'Huỳnh', 'Minh DD', 'Nam', '0919000028', '128 Đường Số 4, Quận 8, TP.HCM', 1),
                                                                                                   ('RD29', 'Nguyễn', 'Lan EE', 'Nữ', '0920000029', '129 Đường Số 5, Quận 9, TP.HCM', 1),
                                                                                                   ('RD30', 'Lê', 'Tuấn FF', 'Nam', '0921000030', '130 Đường Số 6, Quận 10, TP.HCM', 1);


-- Insert Authors (Thêm tác giả)
INSERT INTO `Author` (`firstName`, `lastName`, `isActive`) VALUES
                                                               ('Nguyễn', 'Nhật Ánh', 1),('Tô', 'Hoài', 1),('Nguyễn', 'Du', 1),('Xuân', 'Diệu', 1),('Hồ', 'Xuân Hương', 1),('Lưu', 'Quang Vũ', 1),('Nam', 'Cao', 1),('Victor', 'Hugo', 1),('J.K.', 'Rowling', 1),('George', 'Orwell', 1),('Jane', 'Austen', 1),('Ernest', 'Hemingway', 1),('Gabriel', 'García Márquez', 1),('Toni', 'Morrison', 1),('F. Scott', 'Fitzgerald', 1),('Haruki', 'Murakami', 1), ('Paulo', 'Coelho', 1), ('Agatha', 'Christie', 1),
                                                               ('Dale', 'Carnegie', 1), ('Yuval Noah', 'Harari', 1), ('Stephen', 'Covey', 1), ('Dan', 'Brown', 1), ('Arthur Conan', 'Doyle', 1),
                                                               ('Nguyễn', 'Ngọc Tư', 1), ('Trang', 'Hạ', 1), ('Rosie', 'Nguyễn', 1), ('Tony', 'Buổi Sáng', 1),
                                                               ('Harper', 'Lee', 1), ('Viktor', 'Frankl', 1), ('Antoine de Saint', 'Exupéry', 1), ('Adam', 'Khoo', 1), ('Eiichiro', 'Oda', 1), ('Nguyễn Trí', 'Đoàn', 1), ('James', 'Clear', 1); -- Thêm tác giả còn thiếu

-- Insert Categories (Thêm thể loại)
INSERT INTO `Category` (`name`, `isActive`) VALUES
                                                ('Tiểu thuyết', 1), ('Khoa học', 1), ('Thiếu nhi', 1), ('Tâm lý', 1), ('Kinh doanh', 1),('Lịch sử', 1), ('Trinh thám', 1), ('Khoa học viễn tưởng', 1), ('Tiểu sử', 1), ('Kỹ năng sống', 1), ('Văn học kinh điển', 1), ('Ngoại ngữ', 1), ('Truyện tranh', 1),
                                                ('Nấu ăn', 1), ('Du lịch', 1), ('Nghệ thuật', 1), ('Tôn giáo', 1), ('Sức khỏe', 1), ('Công nghệ thông tin', 1),
                                                ('Truyện ngắn', 1), ('Tản văn', 1), ('Thơ', 1), ('Giáo trình', 1);

-- Insert Publishers (Thêm NXB)
INSERT INTO `Publisher` (`name`, `phone`, `address`, `isActive`) VALUES
                                                                     ('NXB Trẻ', '02839316289', '161 Lý Chính Thắng, Quận 3, TP.HCM', 1),('NXB Kim Đồng', '02439434730', '55 Quang Trung, Hai Bà Trưng, Hà Nội', 1),('NXB Giáo Dục VN', '02438220801', '81 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội', 1),('NXB Văn Học', '0243852740', '18 Nguyễn Trường Tộ, Ba Đình, Hà Nội', 1),('Nhã Nam', '02437759017', '59 Đỗ Quang, Cầu Giấy, Hà Nội', 1), ('Đinh Tị Books', '02473093388', 'Trần Quốc Toản, Hoàn Kiếm, Hà Nội', 1), ('Alphabooks', '02437226234', '176 Thái Hà, Đống Đa, Hà Nội', 1),
                                                                     ('NXB Tổng hợp TP.HCM', '02838225340', '62 Nguyễn Thị Minh Khai, Quận 1, TP.HCM', 1),
                                                                     ('NXB Phụ Nữ Việt Nam', '02439717139', '39 Hàng Chuối, Hai Bà Trưng, Hà Nội', 1),
                                                                     ('Saigon Books', '02838207153', '97 Nguyễn Bỉnh Khiêm, Quận 1, TP.HCM', 1),
                                                                     ('NXB Hội Nhà Văn', '02438452080', '65 Nguyễn Du, Hai Bà Trưng, Hà Nội', 1),
                                                                     ('NXB Lao Động', '02439430260', '175 Giảng Võ, Đống Đa, Hà Nội', 1);

-- Insert Books (Cập nhật authorId và publisherId cho chính xác)
INSERT INTO `Book` (`name`, `categoryId`, `authorId`, `publisherId`, `quantity`, `unitprice`, `yearOfpublication`) VALUES
                                                                                                                       ('Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 1, 1, 1, 50, 85000, 2015),
                                                                                                                       ('Dế Mèn Phiêu Lưu Ký', 3, 2, 2, 65, 78000, 2012),
                                                                                                                       ('Truyện Kiều', 11, 3, 4, 30, 95000, 2005),
                                                                                                                       ('Harry Potter và Hòn Đá Phù Thủy', 1, 9, 1, 70, 145000, 2008),
                                                                                                                       ('Nhà Giả Kim', 1, 17, 5, 80, 92000, 2016),
                                                                                                                       ('Đắc Nhân Tâm', 10, 19, 7, 100, 120000, 2018),
                                                                                                                       ('1984', 11, 10, 5, 45, 115000, 2017),
                                                                                                                       ('Rừng Na Uy', 1, 16, 5, 55, 135000, 2010),
                                                                                                                       ('Giết Con Chim Nhại', 11, 28, 4, 40, 105000, 1960), -- Author: Harper Lee (ID 28)
                                                                                                                       ('Án Mạng Trên Chuyến Tàu Tốc Hành Phương Đông', 7, 18, 1, 60, 99000, 2019),
                                                                                                                       ('Sapiens: Lược Sử Loài Người', 2, 20, 7, 50, 180000, 2015),
                                                                                                                       ('Tiếng Anh Giao Tiếp Cho Người Mất Gốc', 12, 1, 3, 75, 150000, 2022), -- Placeholder Author
                                                                                                                       ('One Piece - Tập 1', 13, 32, 2, 90, 25000, 2008), -- Author: Eiichiro Oda (ID 32)
                                                                                                                       ('Để Con Được Ốm', 10, 33, 6, 60, 88000, 2016), -- Author: Dr. Nguyễn Trí Đoàn (ID 33)
                                                                                                                       ('Atomic Habits', 10, 34, 7, 85, 125000, 2021), -- Author: James Clear (ID 34)
                                                                                                                       ('7 Thói Quen Hiệu Quả', 10, 21, 8, 70, 130000, 2019),
                                                                                                                       ('Mật Mã Da Vinci', 7, 22, 10, 60, 140000, 2006),
                                                                                                                       ('Sherlock Holmes Toàn Tập - Tập 1', 7, 23, 4, 50, 190000, 2015),
                                                                                                                       ('Bí Quyết Nấu Ăn Ngon Mỗi Ngày', 14, 1, 9, 40, 75000, 2020), -- Placeholder Author
                                                                                                                       ('Lập Trình Python Cơ Bản', 19, 1, 8, 55, 160000, 2023), -- Placeholder Author
                                                                                                                       ('Cánh Đồng Bất Tận', 20, 24, 1, 45, 82000, 2005),
                                                                                                                       ('Tuổi Trẻ Đáng Giá Bao Nhiêu?', 10, 26, 11, 90, 89000, 2017), -- Author: Rosie Nguyễn (ID 26), Publisher: NXB Hội Nhà Văn (ID 11)
                                                                                                                       ('Trên Đường Băng', 21, 27, 1, 110, 109000, 2015), -- Author: Tony Buổi Sáng (ID 27)
                                                                                                                       ('Số Đỏ', 11, 7, 4, 35, 70000, 2000),
                                                                                                                       ('Homo Deus: Lược Sử Tương Lai', 2, 20, 7, 40, 185000, 2017),
                                                                                                                       ('Đi Tìm Lẽ Sống', 4, 29, 8, 50, 95000, 2018), -- Author: Viktor Frankl (ID 29)
                                                                                                                       ('Hoàng Tử Bé', 3, 30, 6, 60, 65000, 2013), -- Author: Antoine de Saint-Exupéry (ID 30)
                                                                                                                       ('Tôi Tài Giỏi, Bạn Cũng Thế!', 10, 31, 9, 120, 110000, 2010), -- Author: Adam Khoo (ID 31), Publisher: NXB Phụ Nữ (ID 9)
                                                                                                                       ('Những Người Khốn Khổ', 11, 8, 4, 25, 250000, 2012),
                                                                                                                       ('Cafe Cùng Tony', 21, 27, 1, 100, 98000, 2014); -- Author: Tony Buổi Sáng (ID 27)

-- Insert Suppliers (Thêm nhà cung cấp)
INSERT INTO `Supplier` (`id`, `name`, `phone`, `address`, `isActive`) VALUES
                                                                          ('SUP01', 'FAHASA', '1900636467', 'Nhiều chi nhánh', 1),
                                                                          ('SUP02', 'Phương Nam Book', '19006656', 'Nhiều chi nhánh', 1),
                                                                          ('SUP03', 'Nhà Sách Cá Chép', '02439448946', '211 Xã Đàn, Hà Nội', 1),
                                                                          ('SUP04', 'ADCBook', '18006989', 'Nhiều chi nhánh', 1),
                                                                          ('SUP05', 'TiKi Trading', '19006035', 'Online Platform', 1),
                                                                          ('SUP06', 'NhaSachPhuongDong', '02838383838', '12 Lê Lợi, Q1, TP.HCM', 1),
                                                                          ('SUP07', 'Vinabook.com', '19006401', 'Online Platform', 1),
                                                                          ('SUP08', 'NXB Kim Đồng HCM', '02839390811', '248 Cống Quỳnh, Q1, TP.HCM', 1),
                                                                          ('SUP09', 'Công ty Sách Alpha', '02437226234', '176 Thái Hà, Hà Nội', 1);

-- #####################################################
-- ### GIAO DỊCH MƯỢN TRẢ (Tổng cộng 35 phiếu) ###
-- #####################################################
-- Phiếu 1-15

INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (1, 'RD3', '2023-03-09 00:00:00', '2023-03-23 00:00:00', '2023-03-27 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD15', '2023-02-06 00:00:00', '2023-02-20 00:00:00', '2023-03-01 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD8', '2023-08-08 00:00:00', '2023-08-22 00:00:00', '2023-08-24 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD9', '2023-07-20 00:00:00', '2023-08-03 00:00:00', '2023-08-09 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD9', '2023-07-08 00:00:00', '2023-07-22 00:00:00', '2023-07-24 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD13', '2023-08-19 00:00:00', '2023-09-02 00:00:00', '2023-09-03 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (1, 'RD19', '2024-03-03 00:00:00', '2024-03-17 00:00:00', '2024-03-23 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD13', '2024-01-28 00:00:00', '2024-02-11 00:00:00', '2024-02-18 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD5', '2023-04-28 00:00:00', '2023-05-12 00:00:00', '2023-05-22 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD7', '2023-01-13 00:00:00', '2023-01-27 00:00:00', '2023-02-06 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD20', '2023-03-08 00:00:00', '2023-03-22 00:00:00', '2023-03-29 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD7', '2023-08-11 00:00:00', '2023-08-25 00:00:00', '2023-08-27 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD12', '2023-11-25 00:00:00', '2023-12-09 00:00:00', '2023-12-10 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (7, 'RD16', '2023-10-12 00:00:00', '2023-10-26 00:00:00', '2023-11-02 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD19', '2023-01-22 00:00:00', '2023-02-05 00:00:00', '2023-02-13 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (1, 'RD11', '2023-10-19 00:00:00', '2023-11-02 00:00:00', '2023-11-06 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (1, 'RD12', '2023-03-28 00:00:00', '2023-04-11 00:00:00', '2023-04-16 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD19', '2024-01-21 00:00:00', '2024-02-04 00:00:00', '2024-02-11 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD11', '2023-12-15 00:00:00', '2023-12-29 00:00:00', '2024-01-02 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD5', '2023-04-04 00:00:00', '2023-04-18 00:00:00', '2023-04-23 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD17', '2023-03-19 00:00:00', '2023-04-02 00:00:00', '2023-04-05 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD3', '2023-12-11 00:00:00', '2023-12-25 00:00:00', '2024-01-02 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD9', '2023-01-11 00:00:00', '2023-01-25 00:00:00', '2023-01-26 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD19', '2024-02-16 00:00:00', '2024-03-01 00:00:00', '2024-03-04 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (9, 'RD7', '2023-01-20 00:00:00', '2023-02-03 00:00:00', '2023-02-09 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD1', '2023-02-22 00:00:00', '2023-03-08 00:00:00', '2023-03-10 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD8', '2023-07-06 00:00:00', '2023-07-20 00:00:00', '2023-07-26 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD7', '2023-09-20 00:00:00', '2023-10-04 00:00:00', '2023-10-09 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (10, 'RD8', '2023-11-20 00:00:00', '2023-12-04 00:00:00', '2023-12-13 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD5', '2023-03-04 00:00:00', '2023-03-18 00:00:00', '2023-03-24 00:00:00', 'Phạt');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (7, 'RD10', '2023-02-02 00:00:00', '2023-02-16 00:00:00', '2023-02-15 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (1, 'RD5', '2023-08-01 00:00:00', '2023-08-15 00:00:00', '2023-08-14 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD4', '2024-03-09 00:00:00', '2024-03-23 00:00:00', '2024-03-23 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD15', '2023-07-30 00:00:00', '2023-08-13 00:00:00', '2023-08-13 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD8', '2023-07-10 00:00:00', '2023-07-24 00:00:00', '2023-07-22 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (9, 'RD13', '2024-02-07 00:00:00', '2024-02-21 00:00:00', '2024-02-19 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD17', '2023-11-03 00:00:00', '2023-11-17 00:00:00', '2023-11-17 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD6', '2023-07-26 00:00:00', '2023-08-09 00:00:00', '2023-08-08 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD4', '2023-06-08 00:00:00', '2023-06-22 00:00:00', '2023-06-22 00:00:00', 'Đã_Trả');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD1', '2023-01-14 00:00:00', '2023-01-28 00:00:00', '2023-01-27 00:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (28, 1, 1, 'Quá_Hạn', '2023-03-27 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (21, 2, 1, 'Quá_Hạn', '2023-03-01 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (23, 3, 1, 'Quá_Hạn', '2023-08-24 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (26, 4, 1, 'Quá_Hạn', '2023-08-09 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (19, 5, 1, 'Quá_Hạn', '2023-07-24 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (17, 6, 1, 'Quá_Hạn', '2023-09-03 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (11, 7, 1, 'Quá_Hạn', '2024-03-23 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (30, 8, 1, 'Quá_Hạn', '2024-02-18 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (16, 9, 1, 'Quá_Hạn', '2023-05-22 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (11, 10, 1, 'Quá_Hạn', '2023-02-06 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (9, 11, 1, 'Quá_Hạn', '2023-03-29 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (3, 12, 1, 'Quá_Hạn', '2023-08-27 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (20, 13, 1, 'Quá_Hạn', '2023-12-10 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (28, 14, 1, 'Quá_Hạn', '2023-11-02 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (11, 15, 1, 'Quá_Hạn', '2023-02-13 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (7, 16, 1, 'Quá_Hạn', '2023-11-06 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (27, 17, 1, 'Quá_Hạn', '2023-04-16 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (15, 18, 1, 'Quá_Hạn', '2024-02-11 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (22, 19, 1, 'Quá_Hạn', '2024-01-02 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (14, 20, 1, 'Quá_Hạn', '2023-04-23 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (14, 21, 1, 'Quá_Hạn', '2023-04-05 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (14, 22, 1, 'Quá_Hạn', '2024-01-02 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (4, 23, 1, 'Quá_Hạn', '2023-01-26 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (18, 24, 1, 'Quá_Hạn', '2024-03-04 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (4, 25, 1, 'Quá_Hạn', '2023-02-09 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (14, 26, 1, 'Quá_Hạn', '2023-03-10 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (13, 27, 1, 'Quá_Hạn', '2023-07-26 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (7, 28, 1, 'Quá_Hạn', '2023-10-09 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (22, 29, 1, 'Quá_Hạn', '2023-12-13 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (19, 30, 1, 'Quá_Hạn', '2023-03-24 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (23, 31, 1, 'Đã_Trả', '2023-02-15 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (3, 32, 1, 'Đã_Trả', '2023-08-14 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (13, 33, 1, 'Đã_Trả', '2024-03-23 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (1, 34, 1, 'Đã_Trả', '2023-08-13 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (21, 35, 1, 'Đã_Trả', '2023-07-22 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (10, 36, 1, 'Đã_Trả', '2024-02-19 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (8, 37, 1, 'Đã_Trả', '2023-11-17 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (2, 38, 1, 'Đã_Trả', '2023-08-08 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (21, 39, 1, 'Đã_Trả', '2023-06-22 00:00:00');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (29, 40, 1, 'Đã_Trả', '2023-01-27 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP1', '2023-03-27 00:00:00', 40000.00, 'Đã_Thanh_Toán', 6, '2023-03-28 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP2', '2023-03-01 00:00:00', 25000.00, 'Đã_Thanh_Toán', 5, '2023-03-02 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP3', '2023-08-24 00:00:00', 25000.00, 'Đã_Thanh_Toán', 3, '2023-08-25 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP4', '2023-08-09 00:00:00', 40000.00, 'Đã_Thanh_Toán', 4, '2023-08-10 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP5', '2023-07-24 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP6', '2023-09-03 00:00:00', 40000.00, 'Đã_Thanh_Toán', 2, '2023-09-04 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP7', '2024-03-23 00:00:00', 25000.00, 'Đã_Thanh_Toán', 3, '2024-03-24 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP8', '2024-02-18 00:00:00', 35000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP9', '2023-05-22 00:00:00', 20000.00, 'Đã_Thanh_Toán', 7, '2023-05-23 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP10', '2023-02-06 00:00:00', 40000.00, 'Đã_Thanh_Toán', 9, '2023-02-07 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP11', '2023-03-29 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP12', '2023-08-27 00:00:00', 15000.00, 'Đã_Thanh_Toán', 2, '2023-08-28 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP13', '2023-12-10 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP14', '2023-11-02 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP15', '2023-02-13 00:00:00', 35000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP16', '2023-11-06 00:00:00', 20000.00, 'Đã_Thanh_Toán', 7, '2023-11-07 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP17', '2023-04-16 00:00:00', 35000.00, 'Đã_Thanh_Toán', 10, '2023-04-17 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP18', '2024-02-11 00:00:00', 15000.00, 'Đã_Thanh_Toán', 2, '2024-02-12 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP19', '2024-01-02 00:00:00', 15000.00, 'Đã_Thanh_Toán', 6, '2024-01-03 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP20', '2023-04-23 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP21', '2023-04-05 00:00:00', 30000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP22', '2024-01-02 00:00:00', 15000.00, 'Đã_Thanh_Toán', 10, '2024-01-03 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP23', '2023-01-26 00:00:00', 25000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP24', '2024-03-04 00:00:00', 15000.00, 'Đã_Thanh_Toán', 1, '2024-03-05 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP25', '2023-02-09 00:00:00', 20000.00, 'Đã_Thanh_Toán', 1, '2023-02-10 00:00:00');
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP26', '2023-03-10 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP27', '2023-07-26 00:00:00', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP28', '2023-10-09 00:00:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP29', '2023-12-13 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PP30', '2023-03-24 00:00:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP1', 28, 1, 'Hư sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP2', 21, 2, 'Hư sách', 25000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP3', 23, 3, 'Hư sách', 25000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP4', 26, 4, 'Trả sách trễ 6 ngày', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP5', 19, 5, 'Mất sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP6', 17, 6, 'Hư sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP7', 11, 7, 'Mất sách', 25000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP8', 30, 8, 'Mất sách', 35000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP9', 16, 9, 'Hư sách', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP10', 11, 10, 'Hư sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP11', 9, 11, 'Mất sách', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP12', 3, 12, 'Hư sách', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP13', 20, 13, 'Mất sách', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP14', 28, 14, 'Mất sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP15', 11, 15, 'Hư sách', 35000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP16', 7, 16, 'Trả sách trễ 4 ngày', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP17', 27, 17, 'Trả sách trễ 5 ngày', 35000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP18', 15, 18, 'Hư sách', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP19', 22, 19, 'Hư sách', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP20', 14, 20, 'Trả sách trễ 5 ngày', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP21', 14, 21, 'Mất sách', 30000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP22', 14, 22, 'Mất sách', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP23', 4, 23, 'Trả sách trễ 1 ngày', 25000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP24', 18, 24, 'Trả sách trễ 3 ngày', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP25', 4, 25, 'Mất sách', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP26', 14, 26, 'Trả sách trễ 2 ngày', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP27', 13, 27, 'Mất sách', 40000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP28', 7, 28, 'Trả sách trễ 5 ngày', 15000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP29', 22, 29, 'Hư sách', 20000.00, 1);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PP30', 19, 30, 'Mất sách', 20000.00, 1);

-- ##############################################
-- ### CẬP NHẬT SỐ LƯỢNG SAU KHI INSERT DATA ###
-- ##############################################

-- Bước 1: Cập nhật số lượng sách đang được mượn (`borrowedQuantity`)
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
-- Chạy lệnh này SAU KHI đã insert tất cả dữ liệu sách ban đầu và phiếu nhập.
UPDATE Book b
    INNER JOIN PurchaseOrderDetails pod ON b.id = pod.bookId
    INNER JOIN PurchaseOrders po ON pod.purchaseOrderId = po.id
    SET
        b.quantity = b.quantity + pod.quantity -- Cộng số lượng nhập vào kho
WHERE
    po.status = 'Hoàn_Thành';

-- Cập nhật số lượng đầu sách cho mỗi tác giả dựa trên bảng Book
UPDATE Author a
    JOIN (
    SELECT
    authorId,
    COUNT(id) AS book_count -- Đếm số lượng sách (theo ID sách) cho mỗi authorId
    FROM
    Book
    GROUP BY
    authorId -- Nhóm kết quả theo authorId để đếm riêng cho từng tác giả
    ) AS book_counts ON a.id = book_counts.authorId -- Kết nối bảng Author với kết quả đếm dựa trên ID
    SET
        a.quantity = book_counts.book_count; -- Cập nhật cột quantity của Author bằng số lượng sách đếm được

-- Optional: Cập nhật số lượng về 0 cho những tác giả không có sách nào trong bảng Book
-- (Những tác giả này sẽ không được cập nhật bởi câu lệnh JOIN ở trên)
UPDATE Author
SET quantity = 0
WHERE id NOT IN (SELECT DISTINCT authorId FROM Book); -- Tìm những authorId không tồn tại trong bảng Book

-- Xóa cột complianceCount khỏi bảng Reader
ALTER TABLE `Reader`
DROP COLUMN `complianceCount`;


