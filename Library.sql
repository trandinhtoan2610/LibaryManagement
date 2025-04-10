CREATE TABLE `Role` (
                        `id` bigint PRIMARY KEY AUTO_INCREMENT,
                        `name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `Employee` (
                            `id` bigint PRIMARY KEY AUTO_INCREMENT,
                            `firstName` varchar(255) NOT NULL,
                            `lastName` varchar(255) NOT NULL,
                            `gender` ENUM ('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                            `username` varchar(255) UNIQUE NOT NULL,
                            `password` varchar(255) NOT NULL,
                            `roleId` bigint NOT NULL,
                            `phone` varchar(20) NOT NULL,
                            `address` varchar(255) NOT NULL,
                            `salary` bigint NOT NULL
);

CREATE TABLE `Reader` (
                          `id` bigint PRIMARY KEY AUTO_INCREMENT,
                          `firstName` varchar(255) NOT NULL,
                          `lastName` varchar(255) NOT NULL,
                          `gender` ENUM ('Nam', 'Nữ') NOT NULL DEFAULT 'Nam',
                          `phone` varchar(10) NOT NULL,
                          `address` varchar(255) NOT NULL
);

CREATE TABLE `Author` (
                          `id` bigint PRIMARY KEY AUTO_INCREMENT,
                          `firstName` varchar(255) NOT NULL,
                          `lastName` varchar(255) NOT NULL,
                          `quantity` int NOT NULL DEFAULT 0
);

CREATE TABLE `Category` (
                            `id` bigint PRIMARY KEY AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL
);

CREATE TABLE `Book` (
                        `id` bigint PRIMARY KEY AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        `categoryId` bigint NOT NULL,
                        `authorId` bigint NOT NULL,
                        `publisherId` bigint NOT NULL,
                        `quantity` int NOT NULL,
                        `unitprice` bigint NOT NULL,
                        `yearOfpublication` year
);

CREATE TABLE `Publisher` (
                             `id` bigint PRIMARY KEY AUTO_INCREMENT,
                             `name` varchar(255) NOT NULL,
                             `phone` varchar(10) NOT NULL,
                             `address` varchar(255) NOT NULL
);

CREATE TABLE `Borrow_in_Sheet` (
                                   `id` bigint PRIMARY KEY AUTO_INCREMENT,
                                   `employeeId` bigint NOT NULL,
                                   `readerId` bigint NOT NULL,
                                   `borrowedDate` datetime NOT NULL DEFAULT (now()),
                                   `duedate` datetime NOT NULL,
                                   `actualReturnDate` datetime NOT NULL,
                                   `status` ENUM ('Đã_Mượn', 'Đã_Trả', 'Quá_Ngày') NOT NULL DEFAULT 'Đã_Mượn'
);

CREATE TABLE `BorrowDetails` (
                                 `borrowId` bigint NOT NULL,
                                 `bookId` bigint NOT NULL,
                                 `quantity` int NOT NULL DEFAULT 1,
                                 `substatus` ENUM ('Đã_Trả', 'Chưa_Trả') NOT NULL DEFAULT 'Chưa_Trả',
                                 PRIMARY KEY (`borrowId`, `bookId`)
);

CREATE TABLE `Supplier` (
                            `id` bigint PRIMARY KEY AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL,
                            `phone` varchar(10) NOT NULL,
                            `address` varchar(255) NOT NULL
);

CREATE TABLE `PurchaseOrders` (
                                  `id` bigint PRIMARY KEY AUTO_INCREMENT,
                                  `supplierId` bigint NOT NULL,
                                  `employeeId` bigint NOT NULL,
                                  `totalAmount` decimal(10,2) NOT NULL DEFAULT 0,
                                  `buyDate` datetime NOT NULL DEFAULT (now()),
                                  `status` ENUM ('Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy') NOT NULL DEFAULT 'Đang_Chờ'
);

CREATE TABLE `PurchaseOrderDetails` (
                                        `purchaseOrderId` bigint NOT NULL,
                                        `bookId` bigint NOT NULL,
                                        `quantity` int NOT NULL DEFAULT 1,
                                        `unitPrice` decimal(10,2) NOT NULL,
                                        `SubTotal` decimal(10,2) NOT NULL,
                                        PRIMARY KEY (`purchaseOrderId`, `bookId`)
);

CREATE TABLE `Penalty` (
                           `Id` bigint PRIMARY KEY NOT NULL,
                           `penaltyDate` datetime NOT NULL,
                           `totalamount` decimal(10,2) NOT NULL,
                           `status` ENUM ('Đã_Thanh_Toán', 'Chưa_Thanh_Toán') NOT NULL DEFAULT 'Chưa_Thanh_Toán'
);

CREATE TABLE `PenaltyDetails` (
                                  `penaltyId` bigint NOT NULL,
                                  `borrowId` bigint NOT NULL,
                                  `bookId` bigint NOT NULL,
                                  `name` varchar(255),
                                  `subamount` decimal(10,2) NOT NULL
);

ALTER TABLE `Employee` ADD FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`categoryId`) REFERENCES `Category` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`authorId`) REFERENCES `Author` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`publisherId`) REFERENCES `Publisher` (`id`);

ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`readerId`) REFERENCES `Reader` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`borrowId`) REFERENCES `Borrow_in_Sheet` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`supplierId`) REFERENCES `Supplier` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`purchaseOrderId`) REFERENCES `PurchaseOrders` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`penaltyId`) REFERENCES `Penalty` (`Id`);

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`borrowId`) REFERENCES `BorrowDetails` (`borrowId`);

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `BorrowDetails` (`bookId`);


INSERT INTO  `Role` (`name`) VALUES
('Admin'),
('Staff'),
('Employee');
INSERT INTO  `Employee` (`firstName`, `lastName`, `gender`, `username`, `password`, `roleId`, `phone`, `address`, `salary`) VALUES
('Nguyễn', 'Thành', 1, 'admin', 'admin', 1, '0123456789', 'Hà Nội', 10000000),
('Trần', 'Thị', 2, 'staff', 'staff', 2, '0987654321', 'Hà Nội', 8000000),
('Lê', 'Văn', 1, 'employee', 'employee', 3, '0912345678', 'Hà Nội', 6000000);
