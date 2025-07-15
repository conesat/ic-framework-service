import * as echarts from 'echarts';

const blueDarkTheme = {
  seriesCnt: '4',
  backgroundColor: 'rgba(0,0,0,0)',
  titleColor: '#516b91',
  subtitleColor: '#93b7e3',
  textColorShow: false,
  textColor: '#333',
  markTextColor: '#eeeeee',
  color: ['#516b91', '#59c4e6', '#edafda', '#93b7e3', '#a5e7f0', '#cbb0e3'],
  borderColor: '#ccc',
  borderWidth: 0,
  visualMapColor: ['#516b91', '#59c4e6', '#a5e7f0'],
  legendTextColor: '#999999',
  kColor: '#edafda',
  kColor0: 'transparent',
  kBorderColor: '#d680bc',
  kBorderColor0: '#8fd3e8',
  kBorderWidth: '2',
  lineWidth: '2',
  symbolSize: '6',
  symbol: 'emptyCircle',
  symbolBorderWidth: '2',
  lineSmooth: true,
  graphLineWidth: 1,
  graphLineColor: '#aaaaaa',
  mapLabelColor: '#000',
  mapLabelColorE: '#516b91',
  mapBorderColor: '#516b91',
  mapBorderColorE: '#516b91',
  mapBorderWidth: 0.5,
  mapBorderWidthE: 1,
  mapAreaColor: '#f3f3f3',
  mapAreaColorE: '#a5e7f0',
  axes: [
    {
      type: 'all',
      name: '通用坐标轴',
      axisLineShow: true,
      axisLineColor: '#cccccc',
      axisTickShow: false,
      axisTickColor: '#333',
      axisLabelShow: true,
      axisLabelColor: '#999999',
      splitLineShow: true,
      splitLineColor: ['#eeeeee'],
      splitAreaShow: false,
      splitAreaColor: ['rgba(250,250,250,0.05)', 'rgba(200,200,200,0.02)'],
    },
    {
      type: 'category',
      name: '类目坐标轴',
      axisLineShow: true,
      axisLineColor: '#333',
      axisTickShow: true,
      axisTickColor: '#333',
      axisLabelShow: true,
      axisLabelColor: '#333',
      splitLineShow: false,
      splitLineColor: ['#ccc'],
      splitAreaShow: false,
      splitAreaColor: ['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.3)'],
    },
    {
      type: 'value',
      name: '数值坐标轴',
      axisLineShow: true,
      axisLineColor: '#333',
      axisTickShow: true,
      axisTickColor: '#333',
      axisLabelShow: true,
      axisLabelColor: '#333',
      splitLineShow: true,
      splitLineColor: ['#ccc'],
      splitAreaShow: false,
      splitAreaColor: ['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.3)'],
    },
    {
      type: 'log',
      name: '对数坐标轴',
      axisLineShow: true,
      axisLineColor: '#333',
      axisTickShow: true,
      axisTickColor: '#333',
      axisLabelShow: true,
      axisLabelColor: '#333',
      splitLineShow: true,
      splitLineColor: ['#ccc'],
      splitAreaShow: false,
      splitAreaColor: ['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.3)'],
    },
    {
      type: 'time',
      name: '时间坐标轴',
      axisLineShow: true,
      axisLineColor: '#333',
      axisTickShow: true,
      axisTickColor: '#333',
      axisLabelShow: true,
      axisLabelColor: '#333',
      splitLineShow: true,
      splitLineColor: ['#ccc'],
      splitAreaShow: false,
      splitAreaColor: ['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.3)'],
    },
  ],
  axisSeperateSetting: false,
  toolboxColor: '#999999',
  toolboxEmphasisColor: '#666666',
  tooltipAxisColor: '#cccccc',
  tooltipAxisWidth: 1,
  timelineLineColor: '#8fd3e8',
  timelineLineWidth: 1,
  timelineItemColor: '#8fd3e8',
  timelineItemColorE: '#8fd3e8',
  timelineCheckColor: '#8fd3e8',
  timelineCheckBorderColor: '#8a7ca8',
  timelineItemBorderWidth: 1,
  timelineControlColor: '#8fd3e8',
  timelineControlBorderColor: '#8fd3e8',
  timelineControlBorderWidth: 0.5,
  timelineLabelColor: '#8fd3e8',
  datazoomBackgroundColor: 'rgba(0,0,0,0)',
  datazoomDataColor: 'rgba(255,255,255,0.3)',
  datazoomFillColor: 'rgba(167,183,204,0.4)',
  datazoomHandleColor: '#a7b7cc',
  datazoomHandleWidth: '100',
  datazoomLabelColor: '#333',
};
echarts.registerTheme('myDark', blueDarkTheme);

export const initLineChart = (dom: any, data: any, echart: any) => {
  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgb(33,33,33)',
      textStyle: {
        color: '#F1F1F3',
      },
      borderColor: 'transparent',
    },
    legend: {
      icon: 'rect',
      itemWidth: 14,
      itemHeight: 5,
      itemGap: 13,
      data: ['2023', '2024', '2025'],
      right: '4%',
      textStyle: {
        fontSize: 12,
        color: '#F1F1F3',
      },
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true,
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        axisLine: {
          lineStyle: {
            color: '#57617B',
          },
        },
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      },
    ],
    yAxis: [
      {
        type: 'value',
        name: '单位（%）',
        axisTick: {
          show: false,
        },
        axisLine: {
          lineStyle: {
            color: '#57617B',
          },
        },
        splitLine: {
          lineStyle: {
            color: 'rgba(0,146,255,0.1)',
          },
        },
      },
    ],
    series: [
      {
        name: '2023',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        showSymbol: false,
        lineStyle: {
          normal: {
            width: 2,
          },
        },
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: 'rgba(137, 189, 27, 0.3)',
                },
                {
                  offset: 0.8,
                  color: 'rgba(137, 189, 27, 0)',
                },
              ],
              false,
            ),
            shadowColor: 'rgba(0, 0, 0, 0.1)',
            shadowBlur: 10,
          },
        },
        itemStyle: {
          normal: {
            color: 'rgb(111,141,39)',
            borderColor: 'rgba(137,189,2,0.27)',
            borderWidth: 12,
          },
        },
        data: [220, 182, 191, 134, 150, 120, 110, 125, 145, 122, 165, 122],
      },
      {
        name: '2024',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        showSymbol: false,
        lineStyle: {
          normal: {
            width: 2,
          },
        },
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: 'rgba(0, 136, 212, 0.3)',
                },
                {
                  offset: 0.8,
                  color: 'rgba(0, 136, 212, 0)',
                },
              ],
              false,
            ),
            shadowColor: 'rgba(0, 0, 0, 0.1)',
            shadowBlur: 10,
          },
        },
        itemStyle: {
          normal: {
            color: 'rgb(100,48,161)',
            borderColor: 'rgba(0,136,212,0.2)',
            borderWidth: 12,
          },
        },
        data: [120, 110, 125, 145, 122, 165, 122, 220, 182, 191, 134, 150],
      },
      {
        name: '2025',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        showSymbol: false,
        lineStyle: {
          normal: {
            width: 2,
          },
        },
        areaStyle: {
          normal: {
            color: new echarts.graphic.LinearGradient(
              0,
              0,
              0,
              1,
              [
                {
                  offset: 0,
                  color: 'rgba(219, 50, 51, 0.3)',
                },
                {
                  offset: 0.8,
                  color: 'rgba(219, 50, 51, 0)',
                },
              ],
              false,
            ),
            shadowColor: 'rgba(0, 0, 0, 0.1)',
            shadowBlur: 10,
          },
        },
        itemStyle: {
          normal: {
            color: 'rgb(50,168,219)',
            borderColor: 'rgba(219,50,51,0.2)',
            borderWidth: 12,
          },
        },
        data: [220, 182, 125, 145, 122, 191, 134, 150, 120, 110, 165, 122],
      },
    ],
  };
  if (!echart) {
    echart = echarts.init(dom, 'dark');
  }
  echart.setOption(option);
  return echart;
};

export const initStateChart = (dom: any, data: any, echart: any) => {
  let option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
    },
    legend: {
      top: '5%',
      left: 'center',
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '56%'],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 10,
          show: true,
        },
        label: {
          show: false,
          position: 'center',
        },
        emphasis: {
          label: {
            show: true,
          },
        },
        labelLine: {
          show: false,
        },
        data: data,
      },
    ],
  };
  if (!echart) {
    echart = echarts.init(dom, 'dark');
  }
  echart.setOption(option);
  return echart;
};
