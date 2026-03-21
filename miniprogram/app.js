App({
  globalData: {
    h5Url: 'http://localhost:5173',  // 开发环境：本地 H5 地址
    // h5Url: 'https://your-domain.com',  // 生产环境：替换为实际部署地址
    token: ''
  },

  onLaunch() {
    // 获取本地存储的 token
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
    }
  }
})
