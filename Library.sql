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
                          `complianceCount` int NOT NULL DEFAULT 0
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
