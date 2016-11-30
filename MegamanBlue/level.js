var SCALE = 3; // The size of canvas divided by sprite_frame_width 

var OFFSET_X = 0; // X offset from sprite background
var OFFSET_Y = 0; // Y offset from sprite background

function Background(game, width, height, image) {
    this.image = image;
    this.width = width;
    this.height = height;
    Entity.call(this, game, 0, 0);
}

Background.prototype = new Entity();
Background.prototype.constructor = Background;

Background.prototype.update = function () {
}

Background.prototype.draw = function (ctx) {
    //debugger;
    ctx.drawImage(this.image, 0, 0, this.width * SCALE, this.height * SCALE);
    Entity.prototype.draw.call(this);
}

function Platform(game, x, y, width, height) {
    this.game = game;
    this.x = (x - OFFSET_X) * SCALE; // Calculations to match up sprite dimensions with canvas dimensions
    this.y = (y - OFFSET_Y) * SCALE;
    this.width = width * SCALE;
    this.height = height * SCALE;
}

Platform.prototype = new Entity();
Platform.prototype.constructor = Platform;

Platform.prototype.update = function() {
}

//Draw red box around the platform for debugging
Platform.prototype.draw = function(ctx) {
    Entity.prototype.draw.call(this);
}

function HealthBar(game, x, y, width, height) {
    this.width = width;
    this.height = height;
    this.lineWidth = 3;
    Entity.call(this, game, x, y);
}

HealthBar.prototype = new Entity();
HealthBar.prototype.constructor = HealthBar;

HealthBar.prototype.update = function() {
    this.x = this.game.camera.curX - 380;
}

HealthBar.prototype.draw = function(ctx){
    this.game.ctx.fillStyle = "gray";
    this.game.ctx.fillRect(this.x, this.y, this.width, this.height);
    if (this.game.player.currentHealth > 0) {
        this.game.player.currentHealth < this.game.player.maxHealth / 3 ? this.game.ctx.fillStyle = "red"
            : this.game.ctx.fillStyle = "green";
        this.game.ctx.fillRect(this.x, this.y, this.width / this.game.player.maxHealth * this.game.player.currentHealth, this.height);
    }
    this.game.ctx.strokeStyle = "black";
    this.game.ctx.lineWidth = this.lineWidth;
    this.game.ctx.strokeRect(this.x, this.y, this.width, this.height);
    for (var i = 0; i < this.game.player.maxHealth; i++) {
        this.game.ctx.beginPath();
        this.game.ctx.moveTo(this.x + i * (this.width / this.game.player.maxHealth), this.y);
        this.game.ctx.lineTo(this.x + i * (this.width / this.game.player.maxHealth), this.y + this.height);
        this.game.ctx.stroke();
    }
}

//Essentially just a Platform but player will handle collision differently.
//May just add an "isLadder" property to platform to replace this. 
function Ladder(game, x, y, width, height) {
    Platform.call(this, game, x, y, width, height);
}

Ladder.prototype = new Platform();
Ladder.prototype.constructor = Ladder;

Ladder.prototype.update = function () {
    Platform.prototype.update.call(this);
}

Ladder.prototype.draw = function (ctx) {
    Platform.prototype.draw.call(this);
}

function Spikes(game, x, y, width, height) {
    Platform.call(this, game, x, y, width, height);
}

Spikes.prototype = new Platform();
Spikes.prototype.constructor = Spikes;

Spikes.prototype.update = function () {
    Platform.prototype.update.call(this);
}

Spikes.prototype.draw = function (ctx) {
    Platform.prototype.draw.call(this);
}

function Powerup(game, x, y, radius, type) {
    this.x = (x - OFFSET_X) * SCALE; // Calculations to match up sprite dimensions with canvas dimensions
    this.y = (y - OFFSET_Y) * SCALE;
    this.radius = radius
    this.type = type;
    this.width = this.radius * 2;
    this.height = this.radius * 2;
    this.started = false;
    Entity.call(this, game, this.x, this.y);
}

Powerup.prototype = new Entity();
Powerup.prototype.constructor = Powerup;

Powerup.prototype.update = function() {
    if (Math.abs(this.x - this.game.player.x) <= 500) this.started = true;
    if (this.started) {
        this.x -= 3;
        this.y += Math.floor(Math.random() * 21) - 10;
        if (this.x < 0) this.removeFromWorld = true;
        Entity.prototype.update.call(this);
    }
}

Powerup.prototype.draw = function(ctx) {
    ctx.beginPath();
    ctx.fillStyle = "purple";
    ctx.arc(this.x + this.radius, this.y - this.radius, this.radius, 0, Math.PI * 2, false);
    ctx.fill();
    ctx.closePath();
    Entity.prototype.draw.call(this);
}

function HealthHeart(game, x, y, platform) {
    this.x = (x - OFFSET_X) * SCALE; // Calculations to match up sprite dimensions with canvas dimensions
    this.y = (y - OFFSET_Y) * SCALE;
    this.width = 20 * SCALE;
    this.height = 25 * SCALE;
    this.platform = platform;
    this.started = false;
    this.game = game;
    this.stillAnimation = new Animation(ASSET_MANAGER.getAsset("./img/health.png"), 0, 0, 20, 25, 0.1, 16, true, true);
    Entity.call(this, game, this.x, this.y);
}

HealthHeart.prototype = new Entity();
HealthHeart.prototype.constructor = HealthHeart;

HealthHeart.prototype.update = function() {
}

HealthHeart.prototype.draw = function(ctx) {
    this.stillAnimation.drawFrame(this.game.clockTick, ctx, this.x, this.y, SCALE);
    Entity.prototype.draw.call(this);
}