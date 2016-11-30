//The Camera is used to follow the player throughout the map. 
//It draws the screen based on where the player currently is. 
function Camera(game, target) {
	this.game = game;
	this.target = target;
	this.origX = this.target.x;
	this.curX = this.origX;
	this.origY = this.target.y;
	this.curY = this.origY;
	this.cameraLock = false;
}

Camera.prototype = new Entity();
Camera.prototype.constructor = Camera;

Camera.prototype.update = function () {
	if (this.game.level == 1) {
		if (this.target.x + (this.game.surfaceWidth - this.game.cameraStart) >= 3346 * 3) {
			if (!this.cameraLock) {
				this.cameraLock = true;
				this.game.player.lockMovement = true;
				var that = this;
				this.game.sounds.ambush.play();
				setTimeout(function() {
					var boss = new Boss(that.game, that.curX + 200, 185 * 3);
					that.game.boss = boss;
					that.game.addEntity(boss);
					that.game.player.lockMovement = false;
				}, 3000);
			}
		}
	} else if (this.game.level == 2) {
		if (this.target.x + (this.game.surfaceWidth - this.game.cameraStart) >= 4441 * 3) {
			if (!this.cameraLock) {
				this.cameraLock = true;
				this.game.player.lockMovement = true;
				var that = this;
				this.game.sounds.ambush.play();
				setTimeout(function() {
					var boss = new Boss(that.game, that.curX + 200, 185 * 3);
					that.game.boss = boss;
					that.game.addEntity(boss);
					that.game.player.lockMovement = false;
				}, 3000);
			}
		}
	}
	if (this.target.x != this.curX && this.target.x > this.game.cameraStart && !this.cameraLock) {
		this.game.ctx.translate(this.curX - this.target.x, 0);
		this.curX = this.target.x;
	}
}