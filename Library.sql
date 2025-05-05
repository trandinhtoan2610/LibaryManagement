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
                                                                                                   ('RD001', 'Nguyễn', 'Văn A', 'Nam', '0911111101', '11 Đường Số 1, Quận 1, TP.HCM', 1),
                                                                                                   ('RD002', 'Trần', 'Thị B', 'Nữ', '0922222202', '22 Đường Số 2, Quận 2, TP.HCM', 1),
                                                                                                   ('RD003', 'Lê', 'Hoàng C', 'Nam', '0933333303', '33 Đường Số 3, Quận 3, TP.HCM', 1),
                                                                                                   ('RD004', 'Phạm', 'Mỹ D', 'Nữ', '0944444404', '44 Đường Số 4, Quận 4, TP.HCM', 1),
                                                                                                   ('RD005', 'Hoàng', 'Gia E', 'Nam', '0955555505', '55 Đường Số 5, Quận 5, TP.HCM', 1),
                                                                                                   ('RD006', 'Đặng', 'Khánh F', 'Nam', '0966666606', '66 Đường Số 6, Quận 6, TP.HCM', 1),
                                                                                                   ('RD007', 'Bùi', 'Thu G', 'Nữ', '0977777707', '77 Đường Số 7, Quận 7, TP.HCM', 1),
                                                                                                   ('RD008', 'Vũ', 'Minh H', 'Nam', '0988888808', '88 Đường Số 8, Quận 8, TP.HCM', 1),
                                                                                                   ('RD009', 'Lý', 'Ngọc I', 'Nữ', '0999999909', '99 Đường Số 9, Quận 9, TP.HCM', 1),
                                                                                                   ('RD010', 'Trương', 'Anh K', 'Nam', '0901000010', '110 Đường Số 10, Quận 10, TP.HCM', 1),
                                                                                                   ('RD011', 'Đinh', 'Tuyết L', 'Nữ', '0902000011', '111 Đường Số 11, Quận 11, TP.HCM', 1),
                                                                                                   ('RD012', 'Ngô', 'Quốc M', 'Nam', '0903000012', '112 Đường Số 12, Quận 12, TP.HCM', 1),
                                                                                                   ('RD013', 'Hà', 'Phương N', 'Nữ', '0904000013', '113 Đường Số 1, Quận Tân Bình, TP.HCM', 1),
                                                                                                   ('RD014', 'Phan', 'Thành O', 'Nam', '0905000014', '114 Đường Số 2, Quận Bình Tân, TP.HCM', 1),
                                                                                                   ('RD015', 'Tạ', 'Diễm P', 'Nữ', '0906000015', '115 Đường Số 3, Quận Phú Nhuận, TP.HCM', 1),
                                                                                                   ('RD016', 'Dương', 'Huy Q', 'Nam', '0907000016', '116 Đường Số 4, Quận Gò Vấp, TP.HCM', 1),
                                                                                                   ('RD017', 'Lương', 'Mai R', 'Nữ', '0908000017', '117 Đường Số 5, Thủ Đức, TP.HCM', 1),
                                                                                                   ('RD018', 'Hoàng', 'Sơn S', 'Nam', '0909000018', '118 Đường Số 6, Bình Chánh, TP.HCM', 1),
                                                                                                   ('RD019', 'Đặng', 'Trà T', 'Nữ', '0910000019', '119 Đường Số 7, Hóc Môn, TP.HCM', 1),
                                                                                                   ('RD020', 'Võ', 'Đức U', 'Nam', '0911000020', '120 Đường Số 8, Củ Chi, TP.HCM', 1),
                                                                                                   ('RD021', 'Nguyễn', 'Thị V', 'Nữ', '0912000021', '121 Đường Số 9, Quận 1, TP.HCM', 1),
                                                                                                   ('RD022', 'Trần', 'Văn X', 'Nam', '0913000022', '122 Đường Số 10, Quận 2, TP.HCM', 1),
                                                                                                   ('RD023', 'Lê', 'Mỹ Y', 'Nữ', '0914000023', '123 Đường Số 11, Quận 3, TP.HCM', 0), -- Reader không hoạt động
                                                                                                   ('RD024', 'Phạm', 'Gia Z', 'Nam', '0915000024', '124 Đường Số 12, Quận 4, TP.HCM', 1),
                                                                                                   ('RD025', 'Hoàng', 'Thị AA', 'Nữ', '0916000025', '125 Đường Số 1, Quận 5, TP.HCM', 1),
                                                                                                   ('RD026', 'Đỗ', 'Văn BB', 'Nam', '0917000026', '126 Đường Số 2, Quận 6, TP.HCM', 1),
                                                                                                   ('RD027', 'Võ', 'Ngọc CC', 'Nữ', '0918000027', '127 Đường Số 3, Quận 7, TP.HCM', 1),
                                                                                                   ('RD028', 'Huỳnh', 'Minh DD', 'Nam', '0919000028', '128 Đường Số 4, Quận 8, TP.HCM', 1),
                                                                                                   ('RD029', 'Nguyễn', 'Lan EE', 'Nữ', '0920000029', '129 Đường Số 5, Quận 9, TP.HCM', 1),
                                                                                                   ('RD030', 'Lê', 'Tuấn FF', 'Nam', '0921000030', '130 Đường Số 6, Quận 10, TP.HCM', 1);


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
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD001', '2023-11-15 10:22:05', '2023-11-29 17:00:00', '2023-11-28 16:15:30', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (1, 1, 1, 'Đã_Trả', '2023-11-28 16:15:30');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD002', '2024-01-10 14:05:11', '2024-01-24 17:00:00', '2024-01-28 09:30:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (4, 2, 1, 'Quá_Hạn', '2024-01-28 09:30:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD005', '2025-04-20 16:30:45', '2025-05-11 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (6, 3, 1, 'Đang_Mượn', NULL);
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (8, 3, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD003', '2024-08-05 09:11:52', '2024-08-26 17:00:00', '2024-09-02 14:20:10', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (10, 4, 1, 'Quá_Hạn', '2024-09-02 14:20:10');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD001', '2025-04-28 11:55:00', '2025-05-19 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (15, 5, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD008', '2024-12-01 08:30:15', '2024-12-15 17:00:00', '2024-12-14 10:05:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (12, 6, 1, 'Đã_Trả', '2024-12-14 10:05:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD010', '2025-04-05 15:00:00', '2025-04-26 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (7, 7, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD015', '2024-11-11 13:45:30', '2024-12-02 17:00:00', '2024-12-10 11:10:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (11, 8, 1, 'Quá_Hạn', '2024-12-10 11:10:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD004', '2025-02-10 09:00:00', '2025-02-24 17:00:00', '2025-02-22 14:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (17, 9, 1, 'Đã_Trả', '2025-02-22 14:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (7, 'RD011', '2025-04-25 10:30:00', '2025-05-16 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (16, 10, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD018', '2024-06-15 11:00:00', '2024-07-06 17:00:00', '2024-07-10 15:45:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (18, 11, 1, 'Quá_Hạn', '2024-07-10 15:45:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (9, 'RD007', '2025-05-01 14:15:00', '2025-05-15 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (13, 12, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (10, 'RD014', '2024-09-20 10:00:00', '2024-10-11 17:00:00', '2024-10-10 11:30:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (11, 13, 1, 'Đã_Trả', '2024-10-10 11:30:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (11, 'RD019', '2025-03-01 16:00:00', '2025-03-22 17:00:00', '2025-03-25 10:00:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (19, 14, 1, 'Quá_Hạn', '2025-03-25 10:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (12, 'RD020', '2025-04-18 13:30:00', '2025-05-09 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (20, 15, 1, 'Đang_Mượn', NULL);

-- Phiếu 16-35
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD026', '2025-05-03 09:15:00', '2025-05-24 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (21, 16, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD027', '2024-10-01 14:00:00', '2024-10-22 17:00:00', '2024-10-20 16:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (22, 17, 1, 'Đã_Trả', '2024-10-20 16:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD028', '2025-01-15 11:20:00', '2025-02-05 17:00:00', '2025-02-10 08:45:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (23, 18, 1, 'Quá_Hạn', '2025-02-10 08:45:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD029', '2025-04-29 15:00:00', '2025-05-20 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (24, 19, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD030', '2024-11-05 10:45:00', '2024-11-26 17:00:00', '2024-11-25 09:30:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (25, 20, 1, 'Đã_Trả', '2024-11-25 09:30:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (7, 'RD002', '2025-05-02 13:00:00', '2025-05-23 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (26, 21, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD006', '2025-01-02 16:10:00', '2025-01-23 17:00:00', '2025-01-20 10:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (27, 22, 1, 'Đã_Trả', '2025-01-20 10:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (9, 'RD012', '2024-07-01 09:30:00', '2024-07-22 17:00:00', '2024-07-25 11:00:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (28, 23, 1, 'Quá_Hạn', '2024-07-25 11:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (10, 'RD016', '2025-04-10 10:15:00', '2025-05-01 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (29, 24, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (11, 'RD021', '2024-12-10 15:30:00', '2024-12-31 17:00:00', '2024-12-30 09:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (30, 25, 1, 'Đã_Trả', '2024-12-30 09:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (13, 'RD009', '2025-05-04 08:30:00', '2025-05-25 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (5, 26, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (14, 'RD013', '2024-05-20 11:00:00', '2024-06-10 17:00:00', '2024-06-08 14:30:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (9, 27, 1, 'Đã_Trả', '2024-06-08 14:30:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (2, 'RD017', '2025-02-18 10:00:00', '2025-03-11 17:00:00', '2025-03-15 16:20:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (1, 28, 1, 'Quá_Hạn', '2025-03-15 16:20:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (3, 'RD022', '2025-04-22 14:45:00', '2025-05-13 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (21, 29, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (4, 'RD024', '2024-08-10 09:00:00', '2024-08-31 17:00:00', '2024-08-30 10:15:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (22, 30, 1, 'Đã_Trả', '2024-08-30 10:15:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (5, 'RD026', '2025-05-04 10:00:00', '2025-05-25 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (3, 31, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (6, 'RD027', '2024-03-10 13:20:00', '2024-03-31 17:00:00', '2024-04-05 15:00:00', 'Phạt');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (14, 32, 1, 'Quá_Hạn', '2024-04-05 15:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (7, 'RD028', '2025-04-27 11:00:00', '2025-05-18 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (25, 33, 1, 'Đang_Mượn', NULL);
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (8, 'RD029', '2024-05-01 10:00:00', '2024-05-22 17:00:00', '2024-05-20 14:00:00', 'Đã_Trả');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (7, 34, 1, 'Đã_Trả', '2024-05-20 14:00:00');
INSERT INTO `Borrow_in_Sheet` (`employeeId`, `readerId`, `borrowedDate`, `duedate`, `actualReturnDate`, `status`) VALUES (9, 'RD030', '2025-05-03 16:30:00', '2025-05-24 17:00:00', NULL, 'Đang_Mượn');
INSERT INTO `BorrowDetails` (`bookId`, `borrowSheetId`, `quantity`, `substatus`, `actualReturnDate`) VALUES (30, 35, 1, 'Đang_Mượn', NULL);


-- ############################################################
-- ### PHIẾU NHẬP SÁCH (Tổng cộng 29 phiếu) ###
-- ############################################################
-- Phiếu Nhập 1-9
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP01', 1, 5650000.00, '2023-09-05 11:30:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (1, 1, 30, 70000.00, 2100000.00), (1, 4, 50, 71000.00, 3550000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP02', 4, 3160000.00, '2024-02-18 15:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (2, 2, 20, 60000.00, 1200000.00), (2, 5, 25, 72000.00, 1800000.00), (2, 13, 10, 16000.00, 160000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP04', 5, 4500000.00, '2025-04-15 09:45:10', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (3, 6, 40, 90000.00, 3600000.00), (3, 15, 10, 90000.00, 900000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP01', 1, 1150000.00, '2024-10-20 14:00:00', 'Đã_Hủy');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (4, 7, 10, 85000.00, 850000.00), (4, 9, 5, 60000.00, 300000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP05', 4, 2875000.00, '2024-07-11 10:10:10', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (5, 10, 25, 80000.00, 2000000.00), (5, 14, 15, 58000.00, 870000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP03', 5, 3375000.00, '2025-01-30 16:20:30', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (6, 15, 30, 100000.00, 3000000.00), (6, 12, 5, 75000.00, 375000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP06', 6, 4900000.00, '2024-11-25 09:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (7, 16, 35, 100000.00, 3500000.00), (7, 17, 10, 140000.00, 1400000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP07', 7, 1250000.00, '2025-05-02 10:00:00', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (8, 13, 50, 20000.00, 1000000.00), (8, 2, 5, 50000.00, 250000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP01', 8, 3200000.00, '2025-03-10 14:30:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (9, 20, 20, 130000.00, 2600000.00), (9, 11, 5, 120000.00, 600000.00);

-- Phiếu Nhập 10-29
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP08', 9, 2460000.00, '2024-04-12 10:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (10, 21, 30, 65000.00, 1950000.00), (10, 24, 10, 51000.00, 510000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP09', 10, 4170000.00, '2024-05-20 11:15:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (11, 22, 40, 72000.00, 2880000.00), (11, 28, 10, 89000.00, 890000.00), (11, 6, 5, 80000.00, 400000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP02', 11, 6250000.00, '2025-04-28 14:00:00', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (12, 29, 25, 200000.00, 5000000.00), (12, 3, 15, 83000.00, 1245000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP03', 13, 2940000.00, '2024-06-18 09:30:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (13, 23, 30, 80000.00, 2400000.00), (13, 30, 6, 90000.00, 540000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP04', 14, 3900000.00, '2024-08-22 16:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (14, 25, 20, 150000.00, 3000000.00), (14, 26, 10, 90000.00, 900000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP05', 2, 1300000.00, '2025-02-05 10:45:00', 'Đã_Hủy');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (15, 27, 20, 50000.00, 1000000.00), (15, 2, 5, 60000.00, 300000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP06', 3, 4400000.00, '2023-12-12 13:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (16, 1, 20, 70000.00, 1400000.00), (16, 5, 30, 70000.00, 2100000.00), (16, 8, 10, 90000.00, 900000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP07', 4, 3600000.00, '2024-01-25 15:20:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (17, 18, 20, 150000.00, 3000000.00), (17, 10, 10, 60000.00, 600000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP08', 5, 2750000.00, '2025-05-04 09:00:00', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (18, 13, 100, 20000.00, 2000000.00), (18, 27, 10, 50000.00, 500000.00), (18, 2, 5, 50000.00, 250000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP09', 6, 5100000.00, '2024-03-05 10:30:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (19, 6, 30, 100000.00, 3000000.00), (19, 15, 20, 90000.00, 1800000.00), (19, 22, 5, 60000.00, 300000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP01', 7, 3850000.00, '2024-04-19 14:50:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (20, 4, 20, 110000.00, 2200000.00), (20, 9, 15, 80000.00, 1200000.00), (20, 17, 5, 90000.00, 450000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP02', 8, 4200000.00, '2025-04-30 16:10:00', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (21, 11, 20, 150000.00, 3000000.00), (21, 20, 10, 120000.00, 1200000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP03', 9, 2050000.00, '2024-07-28 08:45:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (22, 19, 20, 60000.00, 1200000.00), (22, 14, 10, 70000.00, 700000.00), (22, 26, 3, 50000.00, 150000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP04', 10, 3300000.00, '2024-09-15 11:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (23, 7, 15, 90000.00, 1350000.00), (23, 24, 20, 55000.00, 1100000.00), (23, 29, 10, 85000.00, 850000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP05', 11, 1750000.00, '2025-03-20 09:20:00', 'Đã_Hủy');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (24, 8, 10, 100000.00, 1000000.00), (24, 12, 10, 75000.00, 750000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP06', 13, 4800000.00, '2024-10-30 13:40:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (25, 1, 10, 70000.00, 700000.00), (25, 16, 20, 100000.00, 2000000.00), (25, 21, 15, 70000.00, 1050000.00), (25, 28, 10, 80000.00, 800000.00), (25, 30, 3, 85000.00, 255000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP07', 14, 2200000.00, '2024-12-05 15:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (26, 2, 10, 65000.00, 650000.00), (26, 13, 50, 18000.00, 900000.00), (26, 27, 10, 65000.00, 650000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP08', 2, 3500000.00, '2025-05-03 10:10:00', 'Đang_Chờ');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (27, 3, 20, 85000.00, 1700000.00), (27, 18, 10, 180000.00, 1800000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP09', 3, 4000000.00, '2025-02-15 11:30:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (28, 5, 20, 75000.00, 1500000.00), (28, 15, 25, 100000.00, 2500000.00);
INSERT INTO `PurchaseOrders` (`supplierId`, `employeeId`, `totalAmount`, `buyDate`, `status`) VALUES ('SUP01', 4, 3100000.00, '2025-04-01 09:00:00', 'Hoàn_Thành');
INSERT INTO `PurchaseOrderDetails` (`purchaseOrderId`, `bookId`, `quantity`, `unitPrice`, `SubTotal`) VALUES (29, 10, 10, 80000.00, 800000.00), (29, 17, 10, 130000.00, 1300000.00), (29, 25, 10, 100000.00, 1000000.00);

-- ##############################################
-- ### PHIẾU PHẠT (Tổng cộng 9 phiếu) ###
-- ##############################################
-- Phiếu phạt 1-5
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN001', '2024-01-28 09:35:00', 20000.00, 'Đã_Thanh_Toán', 3, '2024-01-29 11:00:00');
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN001', 4, 2, 'Trả muộn 4 ngày', 20000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN002', '2024-09-02 14:25:00', 35000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN002', 10, 4, 'Trả muộn 7 ngày', 35000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN003', '2024-12-10 11:15:20', 40000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN003', 11, 8, 'Trả muộn 8 ngày', 40000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN004', '2024-07-10 15:50:00', 20000.00, 'Đã_Thanh_Toán', 8, '2024-07-11 09:00:00');
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN004', 18, 11, 'Trả muộn 4 ngày', 20000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN005', '2025-03-25 10:05:00', 15000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN005', 19, 14, 'Trả muộn 3 ngày', 15000.00, 1);

-- Phiếu phạt 6-9
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN006', '2025-02-10 08:50:00', 25000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN006', 23, 18, 'Trả muộn 5 ngày', 25000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN007', '2024-07-25 11:05:00', 15000.00, 'Đã_Thanh_Toán', 9, '2024-07-25 11:10:00');
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN007', 28, 23, 'Trả muộn 3 ngày', 15000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN008', '2025-03-15 16:25:00', 20000.00, 'Chưa_Thanh_Toán', NULL, NULL);
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN008', 1, 28, 'Trả muộn 4 ngày', 20000.00, 1);
INSERT INTO `Penalty` (`id`, `penaltyDate`, `totalamount`, `status`, `employeeID`, `payDate`) VALUES ('PEN009', '2024-04-05 15:05:00', 25000.00, 'Đã_Thanh_Toán', 6, '2024-04-06 10:00:00');
INSERT INTO `PenaltyDetails` (`penaltyId`, `bookId`, `borrowId`, `name`, `subamount`, `bookQuantity`) VALUES ('PEN009', 14, 32, 'Trả muộn 5 ngày', 25000.00, 1);


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


