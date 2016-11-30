/**
 * Created by UngDynasty on 5/20/16.
 */
function Boss(game, x, y, platform) {
    this.platform = platform;
    this.scaleBy = 2;
    this.health = 15;
    this.started = false;
    this.shooting = false;
    this.hasShot = false;
    this.invisible = false;
    this.shootAnimation = new Animation(ASSET_MANAGER.getAsset("./img/fBirdShootLeft.gif"), 0, 0, 80, 67, .16, 7, false, false);
    this.stillAnimation = new Animation(ASSET_MANAGER.getAsset("./img/fBirdStill.gif"), 0, 0, 80, 78, 0.15, 4, true, false);
    this.proneShotAnimation = new Animation(ASSET_MANAGER.getAsset("./img/fBirdShot2Left.gif"), 0, 0, 90, 102, 0.16, 8, false, false);
    this.currentAnimation = this.stillAnimation;
    this.startedShooting = false;
    this.hovering = false;
    this.falling = true;
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    this.spawnMinion = false;
    var that = this;
    setInterval(function() {
        that.spawnMinion = true;
    }, 5000);
    Entity.call(this, game, x, y);
}

Boss.prototype = new Entity();
Boss.prototype.constructor = Boss;

Boss.prototype.update = function() {
    if (this.spawnMinion) {
        this.spawnMinion = false;
        this.game.addEntity(new DeadRobot(this.game, this.x / 3 - 5, this.y / 3));
    }
    if (!this.startedShooting) {
        var that = this;
        setInterval(function () {
            that.shooting = true;
        }, 1000);
        this.startedShooting = true;
    }
    for (var i = 0; i < this.game.entities.length; i++) {
        var ent = this.game.entities[i];
        if (this != ent && this.collision(ent)
            && ((ent instanceof Shovel || ent instanceof Projectile) && ent.orig != this)) {
            ent.removeFromWorld = true;
            this.health--;
            console.log("BOSS was hit!");
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
        }
    }

    if (this.health < 1) {
        this.removeFromWorld = true;
        var that = this;
        that.game.sounds.yeehaw.play();
        that.game.mapTransition();
    }

    if (this.falling && !this.hovering) this.y += 5;
    if (this.platform && this.x < this.platform.x) this.falling = true;

    if (this.shooting) {
        if (this.currentAnimation.isDone()) {
            this.currentAnimation.elapsedTime = 0;
            this.shooting = false;
            this.hasShot = false;
        }
        if (!this.hasShot) {
            var p = new FireBall(this.game, this);
            p.setDX(-1);
            this.game.addEntity(p);
            this.hasShot = true;
        }
    }
    Entity.prototype.update.call(this);
}

Boss.prototype.draw = function (ctx) {
    if (this.shooting) {
        if (this.proneShot) {
            this.currentAnimation = this.proneShotAnimation;
        } else {
            this.currentAnimation = this.shootAnimation;
        }
    } else {
        this.currentAnimation = this.stillAnimation;
    }
    if (!this.invisible) this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.prototype.draw.call(this);
}

Boss.prototype.collision = function(other) {
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

function BossHealthBar(game, boss, x, y, width, height) {
    this.width = width;
    this.height = height;
    this.lineWidth = 3;
    this.startHealth = boss.health;
    this.boss = boss;
    Entity.call(this, game, x, y);
}

BossHealthBar.prototype = new Entity();
BossHealthBar.prototype.constructor = BossHealthBar;

BossHealthBar.prototype.update = function() {
    this.x = this.game.camera.curX - 380;
}

BossHealthBar.prototype.draw = function(ctx){
    this.game.ctx.fillStyle = "gray";
    this.game.ctx.fillRect(this.x, this.y, this.width, this.height);
    if (this.boss.health > 0) {
        this.boss.health < this.startHealth / 3 ? this.game.ctx.fillStyle = "red"
            : this.game.ctx.fillStyle = "green";
        this.game.ctx.fillRect(this.x, this.y, this.width / this.startHealth * this.boss.health, this.height);
    }
    this.game.ctx.strokeStyle = "black";
    this.game.ctx.lineWidth = this.lineWidth;
    this.game.ctx.strokeRect(this.x, this.y, this.width, this.height);
    for (var i = 0; i < this.startHealth; i++) {
        this.game.ctx.beginPath();
        this.game.ctx.moveTo(this.x + i * (this.width / this.startHealth), this.y);
        this.game.ctx.lineTo(this.x + i * (this.width / this.startHealth), this.y + this.height);
        this.game.ctx.stroke();
    }
}