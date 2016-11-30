function Jawa(game, x, y, proneShot) {
    this.platform = null;
    this.utini = new Audio("./sound/utini.mp3");
    this.scaleBy = 2.2;
    this.health = 4;
    this.canShootProne = proneShot;
    this.walking = true;
    this.falling = true;
    this.started = false;
    this.shooting = false;
    this.proneShot = false;
    this.hasShot = false;
    this.invisible = false;
    var that = this;
    this.walkAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 3, 8, 28, 37, .2, 4, true, false);
    this.shootAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 158, 99, 39, 35, .2, 3, false, false);
    this.stillAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 31, 45, 28, 37, .2, 1, true, false);
    this.proneShotAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 132, 11, 48, 28, .2, 3, false, false);
    this.currentAnimation = this.stillAnimation;
    this.shootIntervalStarted = false;
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.call(this, game, x * 3, y * 3);
}

Jawa.prototype = new Entity();
Jawa.prototype.constructor = Jawa;

Jawa.prototype.update = function() {
    if (this.onScreen()) this.started = true;
    if (this.started && !this.game.gameOver) {
        if (!this.shootIntervalStarted) {
            var that = this;
            setInterval(function () {
                that.proneShot = that.canShootProne && Math.random() < .5;
                that.shooting = true;
                that.walking = false;
            }, 1000);
            this.shootIntervalStarted = true;
        }
        for (var i = 0; i < this.game.entities.length; i++) {
            var ent = this.game.entities[i];
            if (this != ent && this.collision(ent) 
                && ((ent instanceof Shovel || ent instanceof Projectile) && ent.orig != this)) {
                ent.removeFromWorld = true;
                this.health--;
                this.utini.play();
                console.log("Jawa was hit!");
                var that = this;
                var invisibleInterval = setInterval(function() {
                    that.invisible = !that.invisible;
                }, 50);
                setTimeout(function() {
                    clearInterval(invisibleInterval);
                    that.invisible = false;
                }, 500);
            } else if (ent instanceof Platform && this.collision(ent)) {
                this.falling = false;
                this.platform = ent;
            }
            if (ent instanceof Spikes && this.collision(ent)) {
                this.removeFromWorld = true;
                this.utini.play();
            }
        }

        if (this.health < 1) {
            this.removeFromWorld = true;
            this.game.sounds.deathScream();
        }

        if (this.falling) this.y += 5;
        if (this.walking && (!this.platform || this.x > this.platform.x)) this.x -= 1;
        if (this.platform && this.x < this.platform.x) this.falling = true;

        if (this.shooting) {
            if (this.currentAnimation.isDone()) {
                this.currentAnimation.elapsedTime = 0;
                this.shooting = false;
                this.hasShot = false;
                this.walking = true;
            }
            if (!this.hasShot) {
                if (this.proneShot) {
                    var p = new Projectile(this.game, this, this.y - 10);
                } else {
                    var p = new Projectile(this.game, this, this.y - (2 * this.height / 3));
                }
                p.setDX(-4);
                this.game.addEntity(p);
                if (this.onScreen()) this.game.sounds.shootProjectile();
                this.hasShot = true;
            }
        }
        Entity.prototype.update.call(this);
    }
}

Jawa.prototype.onScreen = function() {
    return Math.abs(this.game.player.x - this.x) <= 400;
}

Jawa.prototype.draw = function (ctx) {
    if (this.shooting) {
        if (this.proneShot) {
            this.currentAnimation = this.proneShotAnimation;
        } else {
            this.currentAnimation = this.shootAnimation;
        }
    } else if (this.walking) { 
        this.currentAnimation = this.walkAnimation;
    } else {
        this.currentAnimation = this.stillAnimation;
    }
    if (!this.invisible) this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.prototype.draw.call(this);
}

Jawa.prototype.collision = function(other) {
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

function Pterofractal(game, x, y, scale, leftEnd, rightEnd) {
    this.animation = new Animation(ASSET_MANAGER.getAsset("./img/pterofractal.png"), 100, 100, 1924, 902, .2, 1, true, false);
    this.rightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/pterofractal.png"), 2076, 96, 1923, 900, .2, 1, true, false);
    this.currentAnimation = this.animation;
    this.facingRight = false;
    this.health = 10;
    this.scaleBy = scale;
    this.leftEnd = leftEnd;
    this.rightEnd = rightEnd;
    this.width = this.currentAnimation.frameWidth * scale;
    this.height = this.currentAnimation.frameHeight * scale;
    this.spawnMinion = false;
    var that = this;
    setInterval(function() {
        if (Math.abs(that.game.player.x - that.x) < 400) that.spawnMinion = true;
    }, 2000);
    Entity.call(this, game, x * 3, y * 3);
}

Pterofractal.prototype = new Entity();
Pterofractal.prototype.constructor = Pterofractal;

Pterofractal.prototype.update = function() {
    if (Math.abs(this.game.player.x - this.x) <= 600) {
        this.game.sounds.howslife.play();
    }
    if (this.health < 1) {
        this.game.gameWon = true;
        this.game.gameOver = true;
        this.removeFromWorld = true;
    }
    if (this.spawnMinion) {
        this.spawnMinion = false;
        this.game.addEntity(new Penguin(this.game, this, (this.x + this.width / 2) / 3, (this.y + 10) / 3, 1));
        // this.game.addEntity(new Gundam(this.game, (this.x + this.width / 2) / 3, (this.y + 10) / 3, 2));
    }
    this.facingRight ? this.x += 5 : this.x -= 5;
    if (this.x < this.leftEnd) this.facingRight = true;
    if (this.x + this.width > this.rightEnd) this.facingRight = false;
    if (this.x + this.width < 0) this.removeFromWorld = true;
    Entity.prototype.update.call(this);
}

Pterofractal.prototype.draw = function(ctx) {
    if (this.facingRight) {
        this.currentAnimation = this.rightAnimation;
    } else {
        this.currentAnimation = this.animation;
    }
    this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.prototype.draw.call(this);
}

function Penguin(game, orig, x, y, scale) {
    this.platform = null;
    this.falling = true;
    this.orig = orig;
    this.health = 3;
    this.invincible = false;
    this.movingRight = false;
    this.invisible = false;
    this.waddleRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/gunter-penguin.png"), 100, 300, 37, 42, .2, 7, true, false);
    this.waddleLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/gunter-penguin.png"), 96, 346, 37, 42, .2, 7, true, true);
    this.currentAnimation = this.waddleLeftAnimation;
    this.width = this.currentAnimation.frameWidth * scale;
    this.height = this.currentAnimation.frameHeight * scale;
    this.scaleBy = scale;
    Entity.call(this, game, x * 3, y * 3);
}

Penguin.prototype = new Entity();
Penguin.prototype.constructor = Penguin;

Penguin.prototype.update = function() {
    if (this.health < 1) {
        this.removeFromWorld = true;
        this.orig.health--;
    }
    if (Math.abs(this.game.player.x - this.x) <= 500) this.started = true;
    if (this.started) {
        if (this.movingRight) {
            this.x += 1;
        } else {
            this.x -= 1;
        }
        for (var i = 0; i < this.game.entities.length; i++) {
            var ent = this.game.entities[i];
            if (ent instanceof Platform && this.collision(ent)) {
                if (this.platform && ent != this.platform) {
                    this.movingRight = !this.movingRight;
                }
                if (!this.platform || this.falling) this.platform = ent;
                this.falling = false;
            } else if (ent instanceof Projectile && this.collision(ent)) {
                ent.removeFromWorld = true;
                this.health--;
                var that = this;
                var invisibleInterval = setInterval(function() {
                    that.invisible = !that.invisible;
                }, 50);
                setTimeout(function() {
                    clearInterval(invisibleInterval);
                    that.invisible = false;
                }, 500);
            }
        }
        if (this.falling) this.y += 5;
        if (this.platform && (this.x < this.platform.x || this.x + this.width 
            > this.platform.x + this.platform.width)) this.falling = true;
        Entity.prototype.update.call(this);
    }
}

Penguin.prototype.draw = function(ctx) {
    if (this.movingRight) {
        this.currentAnimation = this.waddleRightAnimation;
    } else {
        this.currentAnimation = this.waddleLeftAnimation;
    }
    if (!this.invisible) this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.prototype.draw.call(this);
}

Penguin.prototype.collision = function(other) {
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

// function SandPerson(game, x, y) {
//     this.scaleBy = 1.5;
//     this.health = 3;
//     this.tuskenCry = new Audio("./sound/tusken_cry.mp3");
//     this.tuskenCry.play();
//     this.invisible = false;
//     this.raiderAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 178, 196, 38, 70, .3, 2, true, false);
//     this.stillAnimation = new Animation(ASSET_MANAGER.getAsset("./img/jawas.png"), 178, 196, 38, 70, .3, 1, true, false);
//     this.currentAnimation = this.stillAnimation;
//     this.width = this.currentAnimation.frameWidth * this.scaleBy;
//     this.height = this.currentAnimation.frameHeight * this.scaleBy;
//     Entity.call(this, game, x, y);
// }

// SandPerson.prototype = new Entity();
// SandPerson.prototype.constructor = SandPerson;

// SandPerson.prototype.update = function() {
//     for (var i = 0; i < this.game.entities.length; i++) {
//         var ent = this.game.entities[i];
//         if (this != ent && this.collision(ent) && (ent instanceof Shovel || ent instanceof Projectile)) {
//             // this.shooting = true;
//             ent.removeFromWorld = true;
//             this.health--;
//             this.tuskenCry.play();
//             var that = this;
//             var invisibleInterval = setInterval(function() {
//                 that.invisible = !that.invisible;
//             }, 50);
//             setTimeout(function() {
//                 clearInterval(invisibleInterval);
//                 that.invisible = false;
//             }, 500);
//             console.log("SandPerson was hit!");
//         }
//     }

//     if (this.health < 0) {
//         this.removeFromWorld = true;
//         clearInterval(this.cryInterval);
//     }

//     if (this.game.left && this.game.scrolling) this.x+=3 * this.game.scrollSpeed;
//     if (this.game.right && this.game.scrolling) this.x-=3 * this.game.scrollSpeed;
//     Entity.prototype.update.call(this);
// }

// SandPerson.prototype.draw = function (ctx) {
//     this.currentAnimation = this.raiderAnimation;
//     if (!this.invisible) this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
//     this.width = this.currentAnimation.frameWidth * this.scaleBy;
//     this.height = this.currentAnimation.frameHeight * this.scaleBy;
//     Entity.prototype.draw.call(this);
// }

// SandPerson.prototype.collision = function(other) {
//     var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
//                         || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
//                         || (this.x >= other.x && this.x + this.width <= other.x + other.width)
//                         || (other.x >= this.x && other.x + other.width <= this.x + this.width);
//     var collisionY = (this.y <= other.y && this.y >= other.y - other.height)
//                         || (this.y - this.height <= other.y && this.y - this.height >= other.y - other.height)
//                         || (this.y - this.height >= other.y - other.height && this.y <= other.y)
//                         || (other.y <= this.y && other.y - other.height >= this.y - this.height);
//     return collisionX && collisionY;
// }