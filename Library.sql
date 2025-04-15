CREATE TABLE `Role` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE `Employee` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `firstName` VARCHAR(255) NOT NULL,
                            `lastName` VARCHAR(255) NOT NULL,
                            `gender` ENUM ('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                            `username` VARCHAR(255) UNIQUE NOT NULL,
                            `password` VARCHAR(255) NOT NULL,
                            `roleId` BIGINT NOT NULL,
                            `phone` VARCHAR(20) NOT NULL,
                            `address` VARCHAR(255) NOT NULL,
                            `salary` BIGINT NOT NULL
);

CREATE TABLE `Reader` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `gender` ENUM ('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                          `phone` VARCHAR(10) NOT NULL,
                          `address` VARCHAR(255) NOT NULL,
                          `complianceCount` int NOT NULL DEFAULT 3
);

CREATE TABLE `Author` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `firstName` VARCHAR(255) NOT NULL,
                          `lastName` VARCHAR(255) NOT NULL,
                          `quantity` INT NOT NULL DEFAULT 0
);

CREATE TABLE `Category` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `Book` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` VARCHAR(255) NOT NULL,
                        `categoryId` BIGINT NOT NULL,
                        `authorId` BIGINT NOT NULL,
                        `publisherId` BIGINT NOT NULL,
                        `quantity` INT NOT NULL,
                        `unitprice` BIGINT NOT NULL,
                        `yearOfpublication` YEAR
);

CREATE TABLE `Publisher` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `name` VARCHAR(255) NOT NULL,
                             `phone` VARCHAR(10) NOT NULL,
                             `address` VARCHAR(255) NOT NULL
);

CREATE TABLE `Borrow_in_Sheet` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   `employeeId` BIGINT NOT NULL,
                                   `readerId` BIGINT NOT NULL,
                                   `borrowedDate` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                                   `duedate` DATETIME NOT NULL,
                                   `actualReturnDate` DATETIME DEFAULT NULL,
                                   `status` ENUM ('Đang_Mượn', 'Đã_Trả', 'Quá_Ngày') NOT NULL DEFAULT 'Đang_Mượn'
);

CREATE TABLE `BorrowDetails` (
                                 `bookId` BIGINT NOT NULL,
                                 `borrowSheetId` BIGINT NOT NULL,
                                 `quantity` INT NOT NULL DEFAULT 1,
                                 `substatus` ENUM ('Đang_Mượn', 'Chưa_Trả') NOT NULL DEFAULT 'Đang_Mượn',
                                 PRIMARY KEY (`bookId`, `borrowSheetId`)
);

CREATE TABLE `Supplier` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(255) NOT NULL,
                            `phone` VARCHAR(10) NOT NULL,
                            `address` VARCHAR(255) NOT NULL
);

CREATE TABLE `PurchaseOrders` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `supplierId` BIGINT NOT NULL,
                                  `employeeId` BIGINT NOT NULL,
                                  `totalAmount` DECIMAL(10,2) NOT NULL DEFAULT 0,
                                  `buyDate` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP),
                                  `status` ENUM ('Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy') NOT NULL DEFAULT 'Đang_Chờ'
);

CREATE TABLE `PurchaseOrderDetails` (
                                        `purchaseOrderId` BIGINT NOT NULL,
                                        `bookId` BIGINT NOT NULL,
                                        `quantity` INT NOT NULL DEFAULT 1,
                                        `unitPrice` DECIMAL(10,2) NOT NULL,
                                        `SubTotal` DECIMAL(10,2) NOT NULL,
                                        PRIMARY KEY (`purchaseOrderId`, `bookId`)
);

CREATE TABLE `Penalty` (
                           `Id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `penaltyDate` DATETIME NOT NULL,
                           `totalamount` DECIMAL(10,2) NOT NULL,
                           `status` ENUM ('Đã_Thanh_Toán', 'Chưa_Thanh_Toán') NOT NULL DEFAULT 'Chưa_Thanh_Toán'
);

CREATE TABLE `PenaltyDetails` (
                                  `penaltyId` BIGINT NOT NULL,
                                  `bookId` BIGINT NOT NULL,
                                  `borrowId` BIGINT NOT NULL,
                                  `name` VARCHAR(255),
                                  `subamount` DECIMAL(10,2) NOT NULL,
                                  PRIMARY KEY (`penaltyId`, `bookId`, `borrowId`)
);

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

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`penaltyId`) REFERENCES `Penalty` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`bookId`, `borrowId`) REFERENCES `BorrowDetails` (`bookId`, `borrowSheetId`) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO  `Role` (`name`) VALUES
('Admin'),
('Staff'),
('Employee');
INSERT INTO  `Employee` (`firstName`, `lastName`, `gender`, `username`, `password`, `roleId`, `phone`, `address`, `salary`) VALUES
('Nguyễn', 'Thành', 1, 'admin', 'admin', 1, '0123456789', 'Hà Nội', 10000000),
('Trần', 'Thị', 2, 'staff', 'staff', 2, '0987654321', 'Hà Nội', 8000000),
('Lê', 'Văn', 1, 'employee', 'employee', 3, '0912345678', 'Hà Nội', 6000000);


--INSERT INTO Reader (firstName, lastName, gender, phone, address)
--VALUES
--('Nguyễn', 'Minh', 'Nam', '0911111111', 'Hà Nội'),
--('Trần', 'Lan', 'Nữ', '0922222222', 'TP.HCM'),
--('Lê', 'Hùng', 'Nam', '0933333333', 'Đà Nẵng'),
--('Phạm', 'Hòa', 'Nữ', '0944444444', 'Hải Phòng'),
--('Hoàng', 'Khanh', 'Nam', '0955555555', 'Cần Thơ'),
--('Đặng', 'Anh', 'Nữ', '0966666666', 'Huế'),
--('Bùi', 'Tùng', 'Nam', '0977777777', 'Quảng Ninh');
--
--INSERT INTO Author (lastName, firstName)
--VALUES
--('Nguyễn', 'Nhật Ánh'),
--('Tô', 'Hoài'),
--('Nguyễn', 'Du'),
--('Xuân', 'Diệu'),
--('Hồ', 'Xuân Hương'),
--('Lưu', 'Quang Vũ'),
--('Nam', 'Cao');
--
--
--INSERT INTO Category (name)
--VALUES
--('Tiểu thuyết'),
--('Khoa học'),
--('Thiếu nhi'),
--('Tâm lý'),
--('Kinh doanh'),
--('Lịch sử'),
--('Trinh thám');
--
--
--INSERT INTO Publisher (name, phone, address)
--VALUES
--('NXB Trẻ', '0900000001', 'HCM'),
--('NXB Kim Đồng', '0900000002', 'Hà Nội'),
--('NXB Giáo Dục', '0900000003', 'Hà Nội'),
--('NXB Văn Học', '0900000004', 'Đà Nẵng'),
--('NXB Lao Động', '0900000005', 'HCM'),
--('NXB Phụ Nữ', '0900000006', 'Hà Nội'),
--('NXB Hội Nhà Văn', '0900000007', 'Hà Nội');
--
--INSERT INTO Book (name, categoryId, authorId, publisherId, quantity, unitprice, yearOfpublication)
--VALUES
--('Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 1, 1, 1, 10, 80000, 2015),
--('Dế Mèn Phiêu Lưu Ký', 3, 2, 2, 12, 75000, 2012),
--('Truyện Kiều', 1, 3, 4, 8, 90000, 2000),
--('Thơ Tình', 4, 4, 3, 5, 60000, 1999),
--('Bánh Trôi Nước', 4, 5, 5, 6, 50000, 2010),
--('Tôi Và Chúng Ta', 2, 6, 1, 15, 70000, 2005),
--('Chí Phèo', 1, 7, 6, 9, 85000, 1995);
--
--
--INSERT INTO Supplier (name, phone, address)
--VALUES
--('Công ty Sách ABC', '0901000001', 'Hà Nội'),
--('Công ty Sách XYZ', '0901000002', 'TP.HCM'),
--('Công ty Sách Thiên Long', '0901000003', 'Cần Thơ'),
--('Sách Thành Đô', '0901000004', 'Đà Nẵng'),
--('Sách Hoa Sen', '0901000005', 'Huế'),
--('Sách Tri Thức', '0901000006', 'HCM'),
--('Sách Đông A', '0901000007', 'Hà Nội');
--
--INSERT INTO Borrow_in_Sheet (employeeId, readerId, borrowedDate, duedate, status)
--VALUES
--(1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'Đang_Mượn'),
--(2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY), 'Đã_Trả'),
--(3, 3, NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 'Quá_Ngày'),
--(1, 4, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'Đang_Mượn'),
--(2, 5, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 'Đã_Trả'),
--(3, 6, NOW(), DATE_ADD(NOW(), INTERVAL 6 DAY), 'Đang_Mượn'),
--(1, 7, NOW(), DATE_ADD(NOW(), INTERVAL 9 DAY), 'Quá_Ngày');
--
--
--INSERT INTO BorrowDetails (bookId, borrowSheetId, quantity, substatus)
--VALUES
--(1, 1, 1, 'Đang_Mượn'),
--(2, 2, 2, 'Chưa_Trả'),
--(3, 3, 1, 'Đang_Mượn'),
--(4, 4, 1, 'Chưa_Trả'),
--(5, 5, 2, 'Đang_Mượn'),
--(6, 6, 1, 'Đang_Mượn'),
--(7, 7, 1, 'Chưa_Trả');
