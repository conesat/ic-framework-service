#!/bin/bash
YEAR=$(date +%Y)
MONTH=$(date +%m)
DAY=$(date +%d)
HOUR=$(date +%H)
MINUTE=$(date +%M)
SECOND=$(date +%S)

# 将时间戳写入文件
echo "${YEAR}${MONTH}${DAY}${HOUR}${MINUTE}${SECOND}" > "./assets/version_date"

# 生成资源文件
flutter build ipa --release