export interface Register {
  useType: number;
  fileName: string;
  totalParts: number;
}

export interface UploadSlice {
  file: Blob;
  uploadId: string;
  partNumber: number;
}

export interface UploadSingle {
  file: Blob;
  useType: number;
}
