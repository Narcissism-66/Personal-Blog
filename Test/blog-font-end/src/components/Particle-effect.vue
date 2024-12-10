<template>
  <div style="position: fixed; top: 0; left: 0; bottom: 0; right: 0; z-index: 0">
    <canvas ref="canvas" style="background-color: rgb(50, 64, 87);"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const canvas = ref(null)
const ctx = ref(null)
const width = ref(window.innerWidth)
const height = ref(window.innerHeight)

const dotsNum = 90 // 点的数量
const radius = 1 // 圆的半径，连接线宽度的一半
const fillStyle = 'rgba(255,255,255,0.5)' // 点的颜色
const lineWidth = radius * 2
const connection = 120 // 连线最大距离
const followLength = 80 // 鼠标跟随距离

const dots = ref([])
let animationFrame = null
let mouseX = null
let mouseY = null

function addCanvasSize() { // 改变画布尺寸
  width.value = window.innerWidth
  height.value = window.innerHeight
  canvas.value.width = width.value
  canvas.value.height = height.value
  ctx.value.clearRect(0, 0, width.value, height.value)
  dots.value = []
  if (animationFrame) window.cancelAnimationFrame(animationFrame)
  initDots(dotsNum)
  moveDots()
}

function mouseMove(e) {
  mouseX = e.clientX
  mouseY = e.clientY
}

function mouseOut(e) {
  mouseX = null
  mouseY = null
}

function mouseClick() {
  for (const dot of dots.value) dot.elastic()
}

class Dot {
  constructor(x, y) {
    this.x = x
    this.y = y
    this.speedX = Math.random() * 2 - 1
    this.speedY = Math.random() * 2 - 1
    this.follow = false
  }
  draw() {
    ctx.value.beginPath()
    ctx.value.arc(this.x, this.y, radius, 0, 2 * Math.PI)
    ctx.value.fill()
    ctx.value.closePath()
  }
  move() {
    if (this.x >= width.value || this.x <= 0) this.speedX = -this.speedX
    if (this.y >= height.value || this.y <= 0) this.speedY = -this.speedY
    this.x += this.speedX
    this.y += this.speedY
    if (this.speedX >= 1) this.speedX--
    if (this.speedX <= -1) this.speedX++
    if (this.speedY >= 1) this.speedY--
    if (this.speedY <= -1) this.speedY++
    this.correct()
    this.connectMouse()
    this.draw()
  }
  correct() { // 根据鼠标的位置修正
    if (!mouseX || !mouseY) return
    let lengthX = mouseX - this.x
    let lengthY = mouseY - this.y
    const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
    if (distance <= followLength) this.follow = true
    else if (this.follow === true && distance > followLength && distance <= followLength + 8) {
      let proportion = followLength / distance
      lengthX *= proportion
      lengthY *= proportion
      this.x = mouseX - lengthX
      this.y = mouseY - lengthY
    } else this.follow = false
  }
  connectMouse() { // 点与鼠标连线
    if (mouseX && mouseY) {
      let lengthX = mouseX - this.x
      let lengthY = mouseY - this.y
      const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
      if (distance <= connection) {
        let opacity = (1 - distance / connection) * 0.5
        ctx.value.strokeStyle = `rgba(255,255,255,${opacity})`
        ctx.value.beginPath()
        ctx.value.moveTo(this.x, this.y)
        ctx.value.lineTo(mouseX, mouseY);
        ctx.value.stroke();
        ctx.value.closePath()
      }
    }
  }
  elastic() { // 鼠标点击后的弹射
    let lengthX = mouseX - this.x
    let lengthY = mouseY - this.y
    const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)
    if (distance >= connection) return
    const rate = 1 - distance / connection // 距离越小此值约接近1
    this.speedX = 40 * rate * -lengthX / distance
    this.speedY = 40 * rate * -lengthY / distance
  }
}

function initDots(num) { // 初始化粒子
  ctx.value.fillStyle = fillStyle
  ctx.value.lineWidth = lineWidth
  for (let i = 0; i < num; i++) {
    const x = Math.floor(Math.random() * width.value)
    const y = Math.floor(Math.random() * height.value)
    const dot = new Dot(x, y)
    dot.draw()
    dots.value.push(dot)
  }
}

function moveDots() { // 移动并建立点与点之间的连接线
  ctx.value.clearRect(0, 0, width.value, height.value)
  for (const dot of dots.value) {
    dot.move()
  }
  for (let i = 0; i < dots.value.length; i++) {
    for (let j = i; j < dots.value.length; j++) {
      const distance = Math.sqrt((dots.value[i].x - dots.value[j].x) ** 2 + (dots.value[i].y - dots.value[j].y) ** 2)
      if (distance <= connection) {
        let opacity = (1 - distance / connection) * 0.5
        ctx.value.strokeStyle = `rgba(255,255,255,${opacity})`
        ctx.value.beginPath()
        ctx.value.moveTo(dots.value[i].x, dots.value[i].y)
        ctx.value.lineTo(dots.value[j].x, dots.value[j].y);
        ctx.value.stroke();
        ctx.value.closePath()
      }
    }
  }
  animationFrame = window.requestAnimationFrame(moveDots)
}

onMounted(() => {
  ctx.value = canvas.value.getContext('2d')
  addCanvasSize()
  initDots(dotsNum)
  moveDots()

  document.onmousemove = mouseMove
  document.onmouseout = mouseOut
  document.onclick = mouseClick
  window.onresize = addCanvasSize
})

onBeforeUnmount(() => {
  if (animationFrame) window.cancelAnimationFrame(animationFrame)
  document.onmousemove = null
  document.onmouseout = null
  document.onclick = null
  window.onresize = null
})
</script>

<style scoped>
</style>