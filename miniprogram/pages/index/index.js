const app = getApp()

Page({
  data: {
    h5Url: ''
  },

  onLoad() {
    const token = wx.getStorageSync('token')
    let url = app.globalData.h5Url

    // 将 token 传递给 H5，H5 侧通过 URL hash 或 localStorage 获取
    if (token) {
      url = `${url}?token=${token}`
    }

    this.setData({ h5Url: url })
  },

  // 接收 H5 通过 wx.miniProgram.postMessage 发送的消息
  handleMessage(e) {
    const data = e.detail.data
    if (data && data.length > 0) {
      const msg = data[data.length - 1]
      // 处理 token 同步
      if (msg.type === 'setToken') {
        wx.setStorageSync('token', msg.token)
        app.globalData.token = msg.token
      }
      // 处理退出登录
      if (msg.type === 'logout') {
        wx.removeStorageSync('token')
        app.globalData.token = ''
        this.setData({ h5Url: app.globalData.h5Url + '/login' })
      }
    }
  }
})
