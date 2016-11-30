function Projectile(game, orig, y) {
    this.game = game;
    this.orig = orig;
    this.scaleBy = this.orig.scaleBy;
    this.dx = 0;
    this.dy = 0;
    this.leftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 177, 76, 17, 13, .1, 2, true, false);
    this.rightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 173, 58, 17, 13, .1, 2, true, true);
    y ? this.y = y : this.y = (this.orig.y + this.orig.y - this.orig.height) / 2;
    this.currentAnimation = this.leftAnimation;
}

Projectile.prototype = new Entity();
Projectile.prototype.constructor = Projectile;

Projectile.prototype.update = function() {
    for (var i = 0; i < this.game.entities.length; i++) {
        var ent = this.game.entities[i];
        if ((ent instanceof Platform || ent instanceof Ladder || ent instanceof Spikes) && this.collision(ent)) {
            this.removeFromWorld = true;
        }
    }
    if (this.x < 0) this.removeFromWorld = true;
    this.x += this.dx * this.game.scrollSpeed;
    this.y += this.dy * this.game.scrollSpeed;
    Entity.prototype.update.call(this);
}

Projectile.prototype.draw = function(ctx) {
    this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    Entity.prototype.draw.call(this);
}

Projectile.prototype.setDX = function(dx) {
    this.dx = dx;
    if (dx > 0) {
        this.x = this.orig.x + this.orig.width + 15;
        this.currentAnimation = this.rightAnimation;
    } else {
        this.x = this.orig.x - 15;
        this.currentAnimation = this.leftAnimation;
    }
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
}

Projectile.prototype.collision = function(other) {
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

function Shovel(game, MegaMan, dx, dy) {
    this.game = game;
    this.orig = MegaMan;
    this.scaleBy = this.orig.scaleBy;
    this.x = this.orig.x + this.orig.width + 10;
    this.dx = dx;
    this.dy = dy;
    if (this.dx >= 0) {
        this.x = this.orig.x + this.orig.shootAnimation.frameWidth * this.scaleBy + 10;
    } else {
        this.x = this.orig.x - this.width - 10;
    }
    this.y = this.orig.y - this.orig.shootAnimation.frameHeight * this.orig.scaleBy / 2;
    this.animation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 838, 635, 45, 13, 1.0, 1, true, true);
    this.width = this.animation.frameWidth * this.scaleBy;
    this.height = this.animation.frameHeight * this.scaleBy;
}

Shovel.prototype = new Entity();
Shovel.prototype.constructor = Shovel;

Shovel.prototype.update = function() {
    this.x += this.dx;
    this.y += this.dy;
    if (this.x > 800) this.removeFromWorld = true;
    Entity.call(this, this.game, this.x, this.y);
}

Shovel.prototype.draw = function(ctx) {
    this.animation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    Entity.prototype.draw.call(this);
}

function FireBall(game, orig, y) {
    this.game = game;
    this.orig = orig;
    this.scaleBy = this.orig.scaleBy;
    this.dx = 0;
    this.dy = 0;
    this.leftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/fireball.gif"), 0, 8, 50, 47, 0.2, 4, true, true);
    y ? this.y = y : this.y = (this.orig.y + this.orig.y - this.orig.height + 50) / 2;
    this.currentAnimation = this.leftAnimation;
}

FireBall.prototype = new Entity();
FireBall.prototype.constructor = FireBall;

FireBall.prototype.update = function() {
    for (var i = 0; i < this.game.entities.length; i++) {
        var ent = this.game.entities[i];
        if ((ent instanceof Platform || ent instanceof Ladder || ent instanceof Spikes) && this.collision(ent)) {
            this.removeFromWorld = true;
        }
    }
    if (this.x < 0) this.removeFromWorld = true;
    this.x += this.dx * this.game.scrollSpeed;
    this.y += this.dy * this.game.scrollSpeed;
    Entity.prototype.update.call(this);
}

FireBall.prototype.draw = function(ctx) {
    this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    Entity.prototype.draw.call(this);
}

FireBall.prototype.setDX = function(dx) {
    this.dx = dx;
    dx > 0 ? this.currentAnimation = this.rightAnimation : this.currentAnimation = this.leftAnimation;
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    dx > 0 ? this.x = this.orig.x + this.orig.width + 15 : this.x = this.orig.x - this.width - 15;
}

FireBall.prototype.collision = function(other) {
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