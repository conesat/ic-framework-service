// ECharts主题配置
export const lightTheme = {
  // 背景色
  backgroundColor: '#ffffff',

  // 文字颜色
  textStyle: {
    color: '#333333'
  },

  // 标题样式
  title: {
    textStyle: {
      color: '#333333'
    },
    subtextStyle: {
      color: '#666666'
    }
  },

  // 图例样式
  legend: {
    textStyle: {
      color: '#666666'
    }
  },

  // 坐标轴样式
  xAxis: {
    axisLine: {
      lineStyle: {
        color: '#e5e5e5'
      }
    },
    axisTick: {
      lineStyle: {
        color: '#e5e5e5'
      }
    },
    axisLabel: {
      color: '#666666'
    },
    splitLine: {
      lineStyle: {
        color: '#f0f0f0'
      }
    }
  },

  yAxis: {
    axisLine: {
      lineStyle: {
        color: '#e5e5e5'
      }
    },
    axisTick: {
      lineStyle: {
        color: '#e5e5e5'
      }
    },
    axisLabel: {
      color: '#666666'
    },
    splitLine: {
      lineStyle: {
        color: '#f0f0f0'
      }
    }
  },

  // 提示框样式
  tooltip: {
    backgroundColor: '#ffffff',
    borderColor: '#e5e5e5',
    textStyle: {
      color: '#333333'
    }
  },

  // 数据区域缩放组件
  dataZoom: {
    textStyle: {
      color: '#666666'
    }
  },

  // 时间轴
  timeline: {
    lineStyle: {
      color: '#e5e5e5'
    },
    itemStyle: {
      color: '#0052d9'
    },
    label: {
      color: '#666666'
    },
    controlStyle: {
      color: '#0052d9',
      borderColor: '#0052d9'
    }
  } as any,

  // 颜色配置
  color: [
    '#0052d9', // 主色调
    '#00a870', // 成功色
    '#ed7b2f', // 警告色
    '#d54941', // 错误色
    '#059669', // 绿色
    '#f39c12', // 橙色
    '#9b59b6', // 紫色
    '#1abc9c', // 青色
    '#34495e', // 深灰
    '#95a5a6'  // 浅灰
  ]
}

export const darkTheme = {
  // 背景色
  backgroundColor: '#1a1a1a',

  // 文字颜色
  textStyle: {
    color: '#ffffff'
  },

  // 标题样式
  title: {
    textStyle: {
      color: '#ffffff'
    },
    subtextStyle: {
      color: '#cccccc'
    }
  },

  // 图例样式
  legend: {
    textStyle: {
      color: '#cccccc'
    }
  },

  // 坐标轴样式
  xAxis: {
    axisLine: {
      lineStyle: {
        color: '#404040'
      }
    },
    axisTick: {
      lineStyle: {
        color: '#404040'
      }
    },
    axisLabel: {
      color: '#cccccc'
    },
    splitLine: {
      lineStyle: {
        color: '#2a2a2a'
      }
    }
  },

  yAxis: {
    axisLine: {
      lineStyle: {
        color: '#404040'
      }
    },
    axisTick: {
      lineStyle: {
        color: '#404040'
      }
    },
    axisLabel: {
      color: '#cccccc'
    },
    splitLine: {
      lineStyle: {
        color: '#2a2a2a'
      }
    }
  },

  // 提示框样式
  tooltip: {
    backgroundColor: '#2a2a2a',
    borderColor: '#404040',
    textStyle: {
      color: '#ffffff'
    }
  },

  // 数据区域缩放组件
  dataZoom: {
    textStyle: {
      color: '#cccccc'
    }
  },

  // 时间轴
  timeline: {
    lineStyle: {
      color: '#404040'
    },
    itemStyle: {
      color: '#4a9eff'
    },
    label: {
      color: '#cccccc'
    },
    controlStyle: {
      color: '#4a9eff',
      borderColor: '#4a9eff'
    }
  } as any,

  // 颜色配置 - 暗色主题适配的颜色
  color: [
    '#4a9eff', // 主色调 - 更亮的蓝色
    '#00d4aa', // 成功色 - 更亮的绿色
    '#ff9a2e', // 警告色 - 更亮的橙色
    '#ff6b6b', // 错误色 - 更亮的红色
    '#00b8a9', // 青色
    '#ffd93d', // 黄色
    '#6c5ce7', // 紫色
    '#a8e6cf', // 浅绿色
    '#ff8b94', // 粉色
    '#96ceb4'  // 薄荷绿
  ]
}

// 主题管理类
export class EChartsThemeManager {
  private static instance: EChartsThemeManager
  private currentTheme: 'light' | 'dark' = 'light'
  private themeObserver: MutationObserver | null = null

  private constructor() {
    this.initTheme()
    this.watchThemeChange()
  }

  public static getInstance(): EChartsThemeManager {
    if (!EChartsThemeManager.instance) {
      EChartsThemeManager.instance = new EChartsThemeManager()
    }
    return EChartsThemeManager.instance
  }

  // 初始化主题
  private initTheme(): void {
    const html = document.documentElement
    this.currentTheme = html.getAttribute('theme-mode') === 'dark' ? 'dark' : 'light'
  }

  // 监听主题变化
  private watchThemeChange(): void {
    this.themeObserver = new MutationObserver(() => {
      const html = document.documentElement
      const newTheme = html.getAttribute('theme-mode') === 'dark' ? 'dark' : 'light'

      if (newTheme !== this.currentTheme) {
        this.currentTheme = newTheme
        this.onThemeChange()
      }
    })

    this.themeObserver.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['theme-mode']
    })
  }

  // 主题变化回调
  private onThemeChange(): void {
    console.log('主题管理器检测到主题变化:', this.currentTheme)
    // 触发主题变化事件
    window.dispatchEvent(new CustomEvent('echarts-theme-change', {
      detail: { theme: this.currentTheme }
    }))
  }

  // 获取当前主题
  public getCurrentTheme(): 'light' | 'dark' {
    return this.currentTheme
  }

  // 获取当前主题配置
  public getCurrentThemeConfig() {
    return this.currentTheme === 'light' ? lightTheme : darkTheme
  }

  // 销毁监听器
  public destroy(): void {
    if (this.themeObserver) {
      this.themeObserver.disconnect()
      this.themeObserver = null
    }
  }
}

// 导出主题管理器实例
export const themeManager = EChartsThemeManager.getInstance()
