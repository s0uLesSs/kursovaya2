create database if not exists javafxTest;
use javafxTest;

CREATE TABLE IF NOT EXISTS `roles` (
  `idRoles` int NOT NULL,
  `roleName` varchar(45) NOT NULL,
  PRIMARY KEY (`idRoles`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `gild` (
  `idGild` int NOT NULL,
  `title` varchar(45) NOT NULL,
  PRIMARY KEY (`idGild`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tables` (
  `idTables` int NOT NULL,
  `tableNumber` varchar(45) NOT NULL,
  PRIMARY KEY (`idTables`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `categories` (
  `idCategories` int NOT NULL,
  `title` varchar(45) NOT NULL,
  `idGild` int NOT NULL,
  PRIMARY KEY (`idCategories`),
  KEY `idGild_idx` (`idGild`),
  CONSTRAINT `idGild` FOREIGN KEY (`idGild`) REFERENCES `gild` (`idGild`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

 CREATE TABLE IF NOT EXISTS `users` (
  `idUsers` int NOT NULL,
  `userCode` varchar(45) NOT NULL,
  `userName` varchar(45) NOT NULL,
  `userSurname` varchar(45) NOT NULL,
  `userRole` int NOT NULL,
  PRIMARY KEY (`idUsers`),
  KEY `idRole_idx` (`userRole`),
  CONSTRAINT `idRole` FOREIGN KEY (`userRole`) REFERENCES `roles` (`idRoles`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `position` (
  `idPosition` int NOT NULL AUTO_INCREMENT,
  `positionName` varchar(45) NOT NULL,
  `positionPrice` int NOT NULL,
  `quantityInStock` int NOT NULL,
  `idCategories` int NOT NULL,
  PRIMARY KEY (`idPosition`),
  KEY `idCategories_idx` (`idCategories`),
  CONSTRAINT `idCategories` FOREIGN KEY (`idCategories`) REFERENCES `categories` (`idCategories`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `orders` (
  `idOrders` int NOT NULL AUTO_INCREMENT,
  `statusOrder` varchar(45) NOT NULL,
  `idUsers` int NOT NULL,
  `idTables` int NOT NULL,
  PRIMARY KEY (`idOrders`),
  KEY `idUsers_idx` (`idUsers`),
  KEY `idTables_idx` (`idTables`),
  CONSTRAINT `idTables` FOREIGN KEY (`idTables`) REFERENCES `tables` (`idTables`),
  CONSTRAINT `idUsers` FOREIGN KEY (`idUsers`) REFERENCES `users` (`idUsers`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `orders_has_position` (
  `Orders_idOrders` int NOT NULL,
  `Position_idPosition` int NOT NULL,
  `quantityPosition` int NOT NULL,
  PRIMARY KEY (`Orders_idOrders`,`Position_idPosition`),
  KEY `fk_Orders_has_Position_Orders1_idx` (`Orders_idOrders`),
  KEY `idPosition_idx` (`Position_idPosition`),
  CONSTRAINT `idOrder` FOREIGN KEY (`Orders_idOrders`) REFERENCES `orders` (`idOrders`),
  CONSTRAINT `idPosition` FOREIGN KEY (`Position_idPosition`) REFERENCES `position` (`idPosition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

INSERT INTO `users` VALUES (1,'30','Али','Свидяков',1),(2,'179','Иван','Коверигин',3),(3,'244','Владислав','Сычев',2),(4,'298','Матвей','Дубков',4);
INSERT INTO `roles` VALUES (1,'Директор'),(2,'Менеджер'),(3,'Официант'),(4,'Кассир');
INSERT INTO `gild` VALUES (1,'Бар'),(2,'Кухня'),(3,'Бизнес-Ланч');
INSERT INTO `categories` VALUES (1,'Горячие Напитки',1),(2,'Лимонады',1),(3,'Прохладительные напитки',1),(4,'Коктейли б/а',1),(5,'Коктейли алк',1),(6,'Крепкий алкоголь',1),(7,'Свежевыжатые соки',1),(8,'Паста',2),(9,'Пицца',2),(10,'Салаты',2),(11,'Супы',2),(12,'Вторые блюда',2),(13,'Выпечка',2),(14,'Гриль',2),(15,'Салаты б/л',3),(16,'Супы б/л',3),(17,'Вторые блюда б/л',3),(18,'Б/л из 2 блюд',3),(19,'Полный б/л',3);
INSERT INTO `tables` VALUES (1,'1.1'),(2,'1.2'),(3,'1.3'),(4,'1.4'),(5,'1.5'),(6,'1.6'),(7,'2.1'),(8,'2.2'),(9,'2.3'),(10,'2.4'),(11,'2.5'),(12,'2.6'),(13,'3.1'),(14,'3.2'),(15,'3.3'),(16,'3.4'),(17,'3.5'),(18,'3.6');
INSERT INTO `position` VALUES (1,'Цезарь с курицей',365,31,10),(2,'Цезарь с креветками',485,18,10),(3,'Хрустящий баклажан',395,43,10),(4,'Теплый салат с пастрами',515,15,10),(5,'Подвяленная свекла',315,30,10),(6,'Борщ с говядиной',285,22,11),(7,'Солянка мясная',315,10,11),(8,'Тыквенный биск',345,0,11),(9,'Куриный бульон',255,14,11),(10,'Паста карбанара',355,40,8),(11,'Паста с креветками',465,35,8),(12,'Паста с базиликом и баклажаном',395,40,8),(13,'Пицца сырная',285,15,9),(14,'Пицца маргарита',315,15,9),(15,'Пицца с креветками',395,15,9),(16,'Пицца с ростбифом',385,15,9),(17,'Бефстроганов',365,20,12),(18,'Печеный картофель',285,30,12),(19,'Щучья котлета',295,22,12),(20,'Салат с курицей',0,20,15),(21,'Б/л из 2 блюд',335,335,18),(22,'Полный б/л',365,365,19),(23,'Кофе Капучино',225,300,1),(24,'Кофе Американо',195,300,1),(25,'Чай черный',310,300,1),(26,'Лимонад ягодный',250,300,2),(27,'Лимонад апельсиновый',250,300,2),(28,'Лимонад киви',275,300,2),(29,'Морс клюквенный',80,300,3),(30,'Сок яблочный',80,300,3),(31,'Пина колада б/а',365,300,4),(32,'Голубая лагуна б/а',295,300,4),(33,'Мохито б/а',315,300,4),(34,'Cherry Sour',415,300,5),(35,'Мартини Тоник',405,300,5),(36,'Мохито алк',395,300,5),(37,'Водка Хаски 50мл',180,300,6),(38,'Виски Джеймсон 50мл',415,300,6),(39,'Коньяк Арарат 50мл',255,300,6),(40,'СВ Яблочный',200,300,7),(41,'СВ Апельсиновый',150,300,7),(42,'СВ Морковный',215,300,7),(43,'Хачапури по-аджарски',265,50,13),(44,'Хачапури по-мегрельски',250,50,13),(45,'Рибай',795,10,14),(46,'Филе-миньон',565,40,14),(47,'Филе цыпленка',295,100,14),(48,'Салат с курицей б/л',0,50,15),(49,'Салат с кальмаром б/л',0,50,15),(50,'Салат греческий б/л',0,50,15),(51,'Борщ б/л',0,50,16),(52,'Логман б/л',0,50,16),(53,'Спагетти Карбанара б/л',0,50,17),(54,'Мит-болы с пюре',0,50,17),(55,'Филе трески в кляре',0,50,17),(56,'Вода негаз',40,500,3);
