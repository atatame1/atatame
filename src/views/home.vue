<template>
  <div class="home-container">
    <!-- 轮播图 - 改为卡片式轮播 -->
    <el-carousel height="300px" :interval="4000" arrow="always" type="card">
      <el-carousel-item v-for="(item, index) in carouselItems" :key="index">
        <div class="carousel-card">
          <img :src="item.image" class="carousel-image" :alt="item.title" />
          <div class="carousel-caption">
            <h3>{{ item.title }}</h3>
            <p>{{ item.description }}</p>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 图片+文字卡片区域 -->
    <div class="content-section">
      <h2 class="section-title">精选内容</h2>
      <div class="card-grid">
        <div 
          v-for="(card, index) in cards" 
          :key="index" 
          class="card"
        >
          <div class="card-image-container">
            <img :src="card.image" class="card-image" :alt="card.title" />
          </div>
          <div class="card-content">
            <h3>{{ card.title }}</h3>
            <p class="card-desc">{{ card.description }}</p>
            <el-button type="primary" plain>
              了解更多 <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowRight } from '@element-plus/icons-vue';

// 轮播图数据
const carouselItems = ref([
  {
    image: '/src/assets/png/awfa.png',
    title: '糖心，值得信赖',
    description: '欢迎来到我们的网站'
  },
  {
    image: '/src/assets/png/VCG211373313541.jpg',
    title: '健康管理',
    description: '健康，在于每天的认真对待'
  },
  {
    image: '/src/assets/png/dawfa.png',
    title: '风险预测',
    description: '创新功能，体验升级'
  }
]);

// 卡片数据
const cards = ref([
  {
    image: '/src/assets/png/image11.png',
    title: '专业团队',
    description: '我们提供高质量的产品和解决方案，采用先进技术，满足您的个性化需求。'
  },
  {
    image: '/src/assets/png/image2.png',
    title: '案例展示',
    description: '查看我们的成功案例，了解如何帮助客户实现业务增长和效率提升。'
  },
  {
    image: '/src/assets/png/image3.png',
    title: '关于我们',
    description: '了解我们的团队文化、发展历程和企业使命，选择值得信赖的合作伙伴。'
  },
  {
    image: '/src/assets/png/image4.png',
    title: '联系我们',
    description: '随时欢迎您的咨询，我们的专业团队将为您提供全方位的支持与服务。'
  }
]);

</script>

<style scoped>
.home-container {
  max-width: 1300px;
  margin: 0 auto;
  padding: 20px;
}

/* 轮播图样式优化 */
:deep(.el-carousel__item) {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.carousel-card {
  position: relative;
  height: 100%;
}

.carousel-image {
  user-select: none;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  padding: 40px 20px 20px;
}

.carousel-caption h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.carousel-caption p {
  font-size: 14px;
  margin: 0;
  opacity: 0.9;
}

/* 卡片区域样式优化 */
.content-section {
  margin-top: 60px;
}

.section-title {
  text-align: center;
  margin-bottom: 40px;
  font-size: 32px;
  color: #333;
  font-weight: 500;
  position: relative;
}

.section-title::after {
  content: '';
  display: block;
  width: 80px;
  height: 4px;
  background: #409eff;
  margin: 10px auto 0;
  border-radius: 2px;
}

.card-grid {
  display: flex;
  gap: 24px;
}

.card {
  width: 290px;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.card-image-container {
  height: 200px;
  overflow: hidden;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.card:hover .card-image {
  transform: scale(1.05);
}

.card-content {
  padding: 20px;
}

.card-content h3 {
  font-size: 18px;
  margin: 0 0 12px 0;
  color: #333;
}

.card-desc {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
  min-height: 60px;
}

.el-button {
  width: 100%;
}
</style>