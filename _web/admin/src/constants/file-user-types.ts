export class FileUseType {
  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }

  id: number;

  name: string;

  toString() {
    return this.name;
  }
}

export const FileUseTypes = {
  private: new FileUseType(-1, '私有图片'),
  public: new FileUseType(0, '公开图片'),
  avatar: new FileUseType(1, '头像'),
  supportingTypeIcon: new FileUseType(2, '配套设施图标'),
  hotelPic: new FileUseType(3, '酒店宣传图'),
  product: new FileUseType(4, '消费商品图'),
};
