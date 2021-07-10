CREATE TRIGGER up_shopify
AFTER UPDATE
ON
Products
FOR EACH ROW
UPDATE Shopify
SET 
	Variant_Barcode = NEW.CodeNumber,
	Type = NEW.GroupName,
	Tags = NEW.Tags,
	Handle = (LOWER(REPLACE((SELECT DescriptiveText
FROM PLUS
WHERE CodeText = NEW.CodeNumber)," ","-"))),
	Title = (SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber),
    Body_HTML = (SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber),
    Variant_Price = (SELECT Prices11 FROM PLUS WHERE CodeText = NEW.CodeNumber),
    Variant_Inventory_Qty = (SELECT StockCurrent FROM PLUS WHERE CodeText = NEW.CodeNumber),
    Variant_SKU = (SUBSTRING((SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber) 
 FROM  (LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber)) + 1) 
 FOR (LOCATE(" ",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber), LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber)))) - (LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber)))))
 
WHERE 
	Variant_Barcode = NEW.CodeNumber
