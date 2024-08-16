const canvas = document.createElement('canvas')
const ctx = canvas.getContext('2d');

document.body.appendChild(canvas);

// Set styles
document.body.setAttribute("style", "margin:0;")
const canvasStyle = "position: fixed;top: 0;left: 0;width: 100%;height: 100%;z-index: -100;";
canvas.setAttribute("style", canvasStyle);

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

window.addEventListener("resize", function(e){
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    init();
})

// window.addEventListener("mousemove", function(e){
//     for(var i=0; i<circles.length; i++){
//         c = circles[i];
//         c.x = e.x;
//         c.y = e.y;
//     }
// })

function Circle(x, y, radius, color){
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.color = color;
    this.alphaState = 1;

    this.draw = function(){
        ctx.save();
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2, false);
        ctx.fillStyle = `rgba(${this.color.r}, ${this.color.g}, ${this.color.b},${this.color.a})`;
        ctx.fill();
        ctx.restore();
    };

    this.update = function(){

        if (this.color.a > 0.5){
            this.alphaState *= -1;
        }
        if(this.color.a < 0.01){
            this.alphaState *= -1;
            this.x = Math.random() * canvas.width;
            this.y = canvas.height * 0.6 + Math.random()*canvas.height * 0.3;
        }
        this.color.a += this.alphaState * 0.003;

    }
}

////////////////////////////////////////////////////////////////////////////////////
function MatrixChar(x, y, color, char){
    this.x = x;
    this.y = y;
    this.color = color || {r:15, g:187, b: 60, a: 1};
    this.char = char || 'M';
    this.font = '20px Consolas';
    this.lastTime = Date.now();

    this.draw = function(){
        ctx.save();
        ctx.font = this.font;
        ctx.fillStyle = `rgba(${this.color.r}, ${this.color.g}, ${this.color.b}, ${this.color.a})`
        ctx.fillText(this.char, this.x, this.y);
        ctx.restore();
    }

    this.update = function(){
        this.y += 1;
        if(this.y > canvas.height){
            this.y = -200;
            // this.x = Math.random() * canvas.width;
        }
        if(Date.now() - this.lastTime > 40){
            this.char = String.fromCharCode(34+Math.random()*33);
            this.lastTime = Date.now();
        }

    }
}

//////////////////////////////////////////////////////////////////////////
function CharLine(x){
    this.x = x;
    this.matrixChars = [];

    this.setX = function(){
        for (let i=0; i<this.matrixChars.length; i++){
            this.matrixChars[i].x =this.x;
        }
    }

    this.update = function(){
        for (let i=0; i<this.matrixChars.length; i++){
            this.matrixChars[i].update();
        }
        this.setX();
        // console.log("line update.");

        // console.log(this.matrixChars[this.matrixChars.length-1].y);
        if (this.matrixChars[this.matrixChars.length-1].y > canvas.height - 10 ){
            this.x = Math.random() * canvas.width;
            this.setX();
            // console.log("set x");
        }
    }

    this.draw = function(){
        for (let i=0; i<this.matrixChars.length; i++){
            this.matrixChars[i].draw();
        }
    }

}

// Star
var circles = [];
// Matrix
var matrix = [];


function init(){
    // init Stars
    circles = [];
    for(var i=0; i< 70; i++){
        c = new Circle(Math.random()*canvas.width,
                        canvas.height * 0.6 + Math.random()*canvas.height * 0.3,
                        2 + Math.random()*15,
                        {'r': 255, 'g': 255, 'b': 255, 'a': Math.random() * 0.5});
        circles.push(c)
    }

    // init matrix chars
    matrix = [];

    for (let ln=0; ln<100;ln++){
        let line = new CharLine(canvas.width * Math.random());
        let lineY = Math.random() * canvas.height;
        for (let i=0; i<10; i++){
            // make a char and add to line
            let color = {r: 255, g: 255, b: 255}
            color.a = 0.35 - i * 0.035;
            let char = new MatrixChar(20, lineY - i * 15, color);
            line.matrixChars.push(char);
        }
        line.setX();
        matrix.push(line);
    }
}


function animate(){
    requestAnimationFrame(animate);

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // ------- UPDATE ------------
    for (let i=0; i<circles.length; i++){
        circles[i].update();
    }
    for (let i=0; i<matrix.length; i++){
        matrix[i].update();
    }

    // ------- DRAW -------------
    for (let i=0; i<circles.length; i++){
        circles[i].draw();
    }
    for (let i=0; i<matrix.length; i++){
        matrix[i].draw();
    }

}



init();
animate();

