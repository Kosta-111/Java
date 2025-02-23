export interface Category {
    id: number;
    name: string;
    image?: string;
    description?: string;
    dateCreated: string;
}

export interface ICategoryCreate {
    name: string;
    description: string;
    imageFile: File | null;
}

export interface ICategoryEdit extends ICategoryCreate {
    id: number;
}