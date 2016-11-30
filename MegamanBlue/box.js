//Creates a new box around the origin. All coordinates are relative to 
//the bottom left corner of origin. 
function Box(orig, x, y, width, height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
}

Box.prototype.collision = function() {
	var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
                        || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
                        || (this.x >= other.x && this.x + this.width <= other.x + other.width)
                        || (other.x >= this.x && other.x + other.width <= this.x + this.width);
    var collisionY = (this.y <= other.y && this.y >= other.y - other.height)
                        || (this.y - this.height <= other.y && this.y - this.height >= other.y - other.height)
                        || (this.y - this.height >= other.y - other.height && this.y <= other.y)
                        || (other.y <= this.y && other.y - other.height >= this.y - this.height);
    return collisionX && collisionY;
}

Box.prototype.collisionAbove = function() {
	var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
                        || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
                        || (this.x >= other.x && this.x + this.width <= other.x + other.width)
                        || (other.x >= this.x && other.x + other.width <= this.x + this.width);
    var collisionY = (this.y - this.height >= other.y - other.height && this.y - this.height <= other.y)
                        && (this.y >= other.y);
    if (collisionY && collisionX) console.log("Collision Above!!!");
    return collisionX && collisionY;
}

Box.prototype.collisionBelow = function() {
	var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
                        || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
                        || (this.x >= other.x && this.x + this.width <= other.x + other.width)
                        || (other.x >= this.x && other.x + other.width <= this.x + this.width);
    var collisionY = (this.y >= other.y - other.height && this.y <= other.y) 
                        && this.y - this.height <= other.y - other.height;
    return collisionX && collisionY;
}

Box.prototype.collisionSide = function() {
	var collisionY = (this.y <= other.y && this.y >= other.y - other.height)
                        || (this.y - this.height <= other.y && this.y - this.height >= other.y - other.height)
                        || (this.y - this.height >= other.y - other.height && this.y <= other.y)
                        || (other.y <= this.y && other.y - other.height >= this.y - this.height);
    var collisionX = (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width
                        || (this.x <= other.x + other.width && this.x > other.x));
    if (collisionY && collisionX) console.log("Collision Right");
    return collisionX && collisionY;
}