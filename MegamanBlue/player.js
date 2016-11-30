//Creates new player who will be controlled by user.
function MegaMan(game, x, y, scaleBy) {
    this.scaleBy = scaleBy;
    this.shootFrequency = 500; //Milliseconds per projectile
    this.facingRight = true;
    this.maxHealth = 10;
    this.currentHealth = this.maxHealth;
    this.jumping = false;
    this.dying = false;
    this.falling = true;
    this.ladder = null;
    this.canShoot = true;
    this.platform = null;
    this.invincible = false;
    this.invisible = false;
    this.deathSoundStarted = false;
    this.lockMovement = false;
    
    //Move left/right animations
    this.runRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 558, 1192, 46, 39, 0.08, 10, true, false);
    this.runLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 558, 1243, 46, 39, 0.08, 10, true, false);
    
    //Crouch/roll animations
    this.crouchAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 4, 1183, 30, 27, 1, 1, true, false);
    this.crouchLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 6, 1117, 30, 27, 0.1, 1, true, true);
    this.rollRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 4, 1183, 30, 27, 0.1, 8, true, false);
    this.rollLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 6, 1117, 30, 27, 0.1, 8, true, true);

    //Climbing animations
    this.climbAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 5, 1030, 28, 52, 0.8, 6, true, true);
    this.stillClimbAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 5, 1030, 28, 52, 0.15, 1, true, true);
    
    //Passive animations
    this.stillAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 4, 56, 35, 41, 0.2, 6, true, false);
    this.faceLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 315, 1035, 35, 41, 0.2, 6, true, false);
    
    //Fighting animations
    this.runRightAndShootAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 290, 504, 43, 37, 0.1, 6, true, false);
    this.runLeftAndShootAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 563, 1297, 43, 37, 0.1, 6, true, true);
    this.shootRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 900, 980, 56, 39, .15, 3, true, false);
    this.shootLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 900, 1075, 56, 39, .15, 3, true, true);

    //Death explosion animation
    this.explosionAnimation = new Animation(ASSET_MANAGER.getAsset("./img/explosion.png"), 0, 0, 96, 96, .15, 15, false, false);
    
    //Jumping/Falling Animations
    this.fallRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 346, 742, 32, 50, .15, 1, true, false);
    this.fallLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 346, 794, 32, 50, .15, 1, true, false);
    this.fallAndShootRightAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 346, 346, 40, 51, .1, 1, true, false);
    this.fallAndShootLeftAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 1024, 85, 40, 50, .1, 1, true, false);
    this.jumpAnimation = new Animation(ASSET_MANAGER.getAsset("./img/MegaSheet.gif"), 4, 1254, 55, 74, 0.08, 8, false, true);

    this.currentAnimation = this.stillAnimation;
    this.width = this.currentAnimation.frameWidth * this.scaleBy;
    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    // this.box = new Box(this, this.x, this.y, this.width, this.height);
    Entity.call(this, game, x, y);
}

MegaMan.prototype = new Entity();
MegaMan.prototype.constructor = MegaMan;

MegaMan.prototype.update = function() {
    if (this.platform && this.collision(this.platform) && this.y > this.platform.y - this.platform.height + 8) {
        // this.y = this.platform.y - this.platform.height;
        if (this.x + this.width >= this.platform.x && this.x + this.width <= this.platform.x + this.platform.width) {
            this.x = this.platform.x - this.width;
        } else {
            this.x = this.platform.x + this.platform.width;
        }
    }

    //Mega Man will die when health below 1
    if (this.currentHealth < 1) {
        this.dying = true;
        if (!this.deathSoundStarted) {
            this.deathSoundStarted = true;
            this.game.sounds.playerDeathSound.play();
        }
    }

    //Once death animation done Mega Man removed from world.
    if (this.dying && this.explosionAnimation.isDone()) {
        this.removeFromWorld = true;
        console.log("Megaman has fallen...");
        this.game.playerCount--;
    }

    //If last move was right, Megaman is looking right
    if (this.game.right && !this.game.left) this.facingRight = true;

    //If last move was left, Megaman is now looking left
    if (this.game.left && !this.game.right) this.facingRight = false;

    //If Megaman runs off edges of his current platform he will start to fall 
    if (this.platform && this.x > this.platform.x + this.platform.width && !this.jumping) this.falling = true; 
    if (this.platform && this.x + this.width < this.platform.x && !this.jumping) this.falling = true;

    //If megaman falls off map HE DEAD!!
    if (this.y - this.height > this.game.ctx.canvas.height) this.currentHealth = 0;

    //If megaman is near a ladder he will not fall
    if (this.ladder && this.collision(this.ladder)) this.falling = false;

    //Changes Megaman's y-position when falling. 
    if (this.falling) this.y+=5;

    //Check for collisions, when done will call different functions based on what
    //Mega Man is colliding with.
    for (var i = 0; i < this.game.entities.length; i++) {
        var ent = this.game.entities[i];
        if ((ent instanceof Projectile || ent instanceof FireBall) && ent.orig != this && this.collision(ent)) {
            ent instanceof Projectile ? this.takeDamage(1) : this.takeDamage(2);
            ent.removeFromWorld = true;
        } else if (ent instanceof Ladder && this.collision(ent)) {
            this.ladder = ent;
            this.falling = false;
            console.log("Near ladder!");
        } else if (ent instanceof Platform && this.collisionBelow(ent) && !this.collisionAbove(ent) && this.falling
                    && this.y - 5 <= ent.y - ent.height) {
            this.falling = false;
            if (this.platform != ent) console.log("Platform Change!");
            this.platform = ent;
        } else if (ent instanceof Platform && ent != this.platform && this.collision(ent) && !this.collisionAbove(ent)) {
            var platformToTheRight = false;
            if (this.x + this.width > ent.x && this.x + this.width <= ent.x + ent.width) {
                this.x = ent.x - this.width;
                platformToTheRight = true;
            } else if (this.x < ent.x + ent.width && this.x > ent.x) {
                this.x = ent.x + ent.width;
            }
            console.log("Player stuck below");
        } else if (ent instanceof Platform && this.collisionAbove(ent)) {
            //If Megaman hits a platform from below and he is jumping, will make him start to fall
            this.jumping = false;
            this.jumpAnimation.elapsedTime = 0;
            this.falling = true;
            console.log("CollisionAbove!");
        } else if (ent instanceof Powerup && this.collision(ent)) {
            ent.removeFromWorld = true;
            var that = this;
            if (ent.type == "rapidfire") {
                this.shootFrequency = 250;
                this.game.sounds.rapidFireUnlock.play();
                setTimeout(function() {
                    that.game.sounds.yeahBaby.play();
                }, this.game.sounds.rapidFireUnlock.duration * 1000);
                setTimeout(function() {
                    that.shootFrequency = 500;
                }, 8000);
            } else if (ent.type == "speedup") {
                this.game.scrollSpeed = 3.0;
                setTimeout(function() {
                    that.game.scrollSpeed = 2.0;
                }, 8000);
            }
        } else if (ent instanceof HealthHeart && this.collision(ent)) {
            ent.removeFromWorld = true;
            this.game.sounds.groovy.play();
            this.currentHealth += 5;
            if (this.currentHealth > this.maxHealth) this.currentHealth = this.maxHealth;
        }
        if (ent instanceof Spikes && this.collision(ent)) {
            this.takeDamage(1);
            this.jumping = true;
        }
        if ((ent instanceof DeadRobot || ent instanceof Gundam || ent instanceof Jawa
             || ent instanceof Boss || ent instanceof Penguin)
            && this.collision(ent)) {
            this.takeDamage(3);
        }
    }

    //Things Mega Man can do when alive
    if (!this.dying) {
        if (this.game.right && !this.game.left && ((this.game.level == 1 && this.x + this.width < 3330 * 3) 
            || (this.game.level == 2 && this.x + this.width < 4441 * 3)) && !this.lockMovement) this.x += this.game.scrollSpeed * 3;
        if (this.game.left && !this.game.right && this.x > 0 && !this.lockMovement) {
            if (!this.game.camera.cameraLock || this.x > this.game.camera.curX - this.game.cameraStart) {
                this.x -= this.game.scrollSpeed * 3;
            }
        }

        //Shooting code
        if (this.game.shooting && this.canShoot) {
            var projectile = new Projectile(this.game, this);
            this.facingRight ? projectile.setDX(5) : projectile.setDX(-5);
            this.game.addEntity(projectile);
            this.game.sounds.shootProjectile();
            this.canShoot = false;
            var that = this;
            setTimeout(function () { // This handles recharge delay.
                that.canShoot = true;
            }, this.shootFrequency);
        }

        // Will be used when Mega Man is colliding with a ladder entity.
        if (!this.game.shooting && this.ladder && this.collision(this.ladder)) {
            if (this.game.up && !this.game.down) this.y -= 5;
            if (this.game.down && !this.game.up && (!this.platform || this.y < this.ladder.y)) this.y += 5;
        }

        //If spacebar is hit and megaman isn't already jumping or falling will initiate new jump.
        if (this.game.space && !this.falling && !this.jumping) {
            this.jumping = true;
        }

        if (this.jumping) {
            if (this.jumpAnimation.isDone()) {
                this.jumpAnimation.elapsedTime = 0;
                this.jumping = false;
                if (this.y < this.platform.y - this.platform.height) this.falling = true;
            }
            this.y -= 7 - this.jumpAnimation.elapsedTime * 10;
        }
    }

    if (this.ladder && this.collision(this.ladder)) this.falling = false;

    Entity.prototype.update.call(this);
}

//Order of animations determines which take priority. For example, if Mega Man is dying then his
//death animation will play because that is the first check that occurs. 
MegaMan.prototype.draw = function (ctx) {
    if (this.dying) {
        this.currentAnimation = this.explosionAnimation;
    } else if (this.jumping && !this.game.shooting) {
        this.currentAnimation = this.jumpAnimation;
    } else if ((this.jumping && this.game.shooting) || (this.falling && this.game.shooting)) {
        this.facingRight ? this.currentAnimation = this.fallAndShootRightAnimation 
                        : this.currentAnimation = this.fallAndShootLeftAnimation;
        if (this.jumping) this.jumpAnimation.elapsedTime += this.game.clockTick;
    } else if (this.falling && !this.game.shooting) {
        this.facingRight ? this.currentAnimation = this.fallRightAnimation
                        : this.currentAnimation = this.fallLeftAnimation;
    } else if (this.game.right && this.game.down && !this.game.left) {
        this.currentAnimation = this.rollRightAnimation;
    } else if (this.game.left && this.game.down && !this.game.right) {
        this.currentAnimation = this.rollLeftAnimation;
    } else if (this.game.right && !this.game.left && this.game.shooting) {
        this.currentAnimation = this.runRightAndShootAnimation;
    } else if (this.game.left && !this.game.right && this.game.shooting) {
        this.currentAnimation = this.runLeftAndShootAnimation;
    } else if (this.game.shooting) {
        this.facingRight ? this.currentAnimation = this.shootRightAnimation
                        : this.currentAnimation = this.shootLeftAnimation;
    } else if ((this.game.up || this.game.down || this.game.right || this.game.left) && this.ladder && this.collision(this.ladder)) {
        this.currentAnimation = this.climbAnimation;
    } else if (this.ladder && this.collision(this.ladder) && !this.game.up) {
        this.currentAnimation = this.stillClimbAnimation;
    } else if (this.game.down) {
        this.facingRight ? this.currentAnimation = this.crouchAnimation
                        : this.currentAnimation = this.crouchLeftAnimation;
    } else if (this.game.right && !this.game.left) {
        this.currentAnimation = this.runRightAnimation;
    } else if (this.game.left && !this.game.right) {
        this.currentAnimation = this.runLeftAnimation;
    } else {
        this.facingRight ? this.currentAnimation = this.stillAnimation
                        : this.currentAnimation = this.faceLeftAnimation;
    }

    //Will not draw megaman when he is invisible (unless he's dying).
    if (!this.invisible || this.dying) {
        this.currentAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, this.scaleBy);
    } else if (this.jumping) {
        this.jumpAnimation.elapsedTime += this.game.clockTick;
    }

    if (this.currentAnimation == this.jumpAnimation) {
        this.width = this.stillAnimation.frameWidth * this.scaleBy;
    } else {
        this.width = this.currentAnimation.frameWidth * this.scaleBy;
    }

    this.height = this.currentAnimation.frameHeight * this.scaleBy;
    Entity.prototype.draw.call(this);
}

//Checks to see if Mega Man is colliding with another object based on width, height, and x, y coordinates.
//Uses rectangular collisions.
MegaMan.prototype.collision = function(other) {
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

MegaMan.prototype.collisionAbove = function(other) {
    var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
                        || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
                        || (this.x >= other.x && this.x + this.width <= other.x + other.width)
                        || (other.x >= this.x && other.x + other.width <= this.x + this.width);
    var collisionY = (this.y - this.height >= other.y - other.height && this.y - this.height <= other.y)
                        && (this.y >= other.y);
    if (collisionY && collisionX) console.log("Collision Above!!!");
    return collisionX && collisionY;
}

MegaMan.prototype.collisionBelow = function(other) {
    var collisionX = (this.x >= other.x && this.x <= other.x + other.width)
                        || (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width)
                        || (this.x >= other.x && this.x + this.width <= other.x + other.width)
                        || (other.x >= this.x && other.x + other.width <= this.x + this.width);
    var collisionY = (this.y >= other.y - other.height && this.y <= other.y) 
                        && this.y - this.height <= other.y - other.height;
    return collisionX && collisionY;
}

MegaMan.prototype.collisionSide = function(other) {
    var collisionY = (this.y <= other.y && this.y >= other.y - other.height)
                        || (this.y - this.height <= other.y && this.y - this.height >= other.y - other.height)
                        || (this.y - this.height >= other.y - other.height && this.y <= other.y)
                        || (other.y <= this.y && other.y - other.height >= this.y - this.height);
    var collisionX = (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width
                        || (this.x <= other.x + other.width && this.x > other.x));
    if (collisionY && collisionX) console.log("Collision Right");
    return collisionX && collisionY;
}

MegaMan.prototype.takeDamage = function(damageAmount) {
    var that = this;
    if (!this.invincible && !this.dying && !this.game.transitioning) {
        this.currentHealth-=damageAmount;
        this.invincible = true;
        var invisibleInterval = setInterval(function() { //Makes megaman blink every 1/10th of a second when he is invincible.
            that.invisible = !that.invisible;
        }, 100);
        setTimeout(function() {
            that.invincible = false;
            clearInterval(invisibleInterval); //Makes Megaman stop blinking when he can take damage again.
            that.invisible = false;
        }, 1000);
        this.game.sounds.ayy.play();
    }
    console.log("MegaMan has been hit! His health is at " + this.currentHealth);
}