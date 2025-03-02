import React, {useEffect, useState} from 'react';
import {useGetProductByIdQuery, useUpdateProductMutation} from "../../services/productsApi.ts";
import { useNavigate, useParams } from 'react-router-dom';
import { IProductEdit } from "../../types/Product.ts";
import { useGetAllCategoriesQuery } from "../../services/categoriesApi.ts";
import { Form, Input, Select } from "antd";
import TextArea from "antd/es/input/TextArea";
import { CloseCircleOutlined } from '@ant-design/icons';
import { SortableContainer, SortableElement } from 'react-sortable-hoc';
import {APP_ENV} from "../../env";

const EditProductPage: React.FC = () => {
    const { id } = useParams<{ id: string }>(); // Отримуємо ID продукту з URL
    const { data: categories, isLoading: categoriesLoading, error: categoriesError } = useGetAllCategoriesQuery();
    const { data: productData, isLoading: isLoadingProduct, error: getProductError } = useGetProductByIdQuery(id!); // Отримуємо продукт
    const [updateProduct, { isLoading, error }] = useUpdateProductMutation();
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const navigate = useNavigate();
    const [form] = Form.useForm<IProductEdit>();

    const categoriesData = categories?.map(item => ({
        label: item.name,
        value: item.id,
    }));

    useEffect(() => {
        if (productData?.images) {
            fetchFiles(productData.images);  // Викликаємо функцію для отримання файлів
        }
    }, [productData]);

    const fetchFiles = async (images: string[]) => {
        const files = await Promise.all(
            images.map(async (imageName: string) => {
                const res = await fetch(APP_ENV.REMOTE_IMAGES_URL + 'large/' + imageName);
                const blobFile = await res.blob();
                return new File([blobFile], imageName, { type: blobFile.type });
            })
        );
        setSelectedFiles(files);
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            const filesArray = Array.from(event.target.files);
            setSelectedFiles(prev => [...prev, ...filesArray]);
        }
    };

    const handleRemoveFile = (index: number) => {
        setSelectedFiles(prev => prev.filter((_, i) => i !== index)); // Видалення файлів
    };

    const onSubmit = async (values: IProductEdit) => {
        try {
            values.id = productData!.id;
            values.imageFiles = selectedFiles;
            // Оновлення продукту
            await updateProduct(values).unwrap();
            navigate('..'); // Перехід після успішного оновлення
        } catch (err) {
            console.error('Error updating product:', err);
        }
    };

    const onSortEnd = ({ oldIndex, newIndex }: { oldIndex: number, newIndex: number }) => {
        const updatedFiles = Array.from(selectedFiles);
        const [removed] = updatedFiles.splice(oldIndex, 1);
        updatedFiles.splice(newIndex, 0, removed);
        setSelectedFiles(updatedFiles);
    };

    const SortableItem = SortableElement(({ file, ind }: { file: File, ind: number }) => (
        <div className="relative">
            <img
                src={URL.createObjectURL(file)}
                alt="preview"
                style={{ maxWidth: "150px", maxHeight: "150px" }}
            />
            <button onClick={() => { handleRemoveFile(ind) }} >
                <CloseCircleOutlined
                    className="absolute top-0 right-0 bg-red-500 text-white rounded-full"
                    style={{ fontSize: '26px' }}
                />
            </button>
        </div>
    ));

    const SortableList = SortableContainer(({ files }: { files: File[] }) => (
        <div className="grid grid-cols-3 gap-4 mt-4">
            {files.map((file, index) => (
                // @ts-ignore
                <SortableItem key={index} index={index} ind={index} file={file} />
            ))}
        </div>
    ));

    if (isLoadingProduct) return <p>Loading...</p>;
    if (getProductError) return <p>Error loading product data.</p>;

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Edit Product</h1>
            <button onClick={() => navigate(-1)}
                    className="px-4 py-2 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-700 mb-4"
            >
                Go Back
            </button>

            <Form form={form} onFinish={onSubmit} layout={"vertical"}>
                <Form.Item
                    label="Назва"
                    name="name"
                    htmlFor="name"
                    initialValue={productData?.name}
                    rules={[
                        { required: true, message: "It is a required field!" },
                        { min: 3, message: "Name must have at least 3 symbols!" },
                    ]}
                >
                    <Input autoComplete="name" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                {categoriesLoading ? (
                    <p>Loading categories...</p>
                ) : categoriesError ? (
                    <p className="text-red-500">Failed to load categories</p>
                ) : (
                    <Form.Item
                        label="Категорія"
                        name="categoryId"
                        htmlFor="categoryId"
                        initialValue={productData?.categoryId}
                        rules={[{ required: true, message: "It is a required field!" }]}
                    >
                        <Select placeholder="Оберіть категорію" options={categoriesData} />
                    </Form.Item>
                )}

                <Form.Item
                    label="Ціна"
                    name="price"
                    htmlFor="price"
                    initialValue={productData?.price}
                    rules={[
                        { required: true, message: "It is a required field!" },
                    ]}
                >
                    <Input type="number" autoComplete="price" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                <Form.Item
                    label="Кількість"
                    name="amount"
                    htmlFor="amount"
                    initialValue={productData?.amount}
                    rules={[
                        { required: true, message: "It is a required field!" },
                    ]}
                >
                    <Input type="number" autoComplete="amount" className={"w-full p-2 border border-gray-300 rounded mt-2"} />
                </Form.Item>

                <Form.Item
                    label="Опис"
                    name="description"
                    htmlFor="description"
                    initialValue={productData?.description}
                    rules={[
                        { required: true, message: "It is a required field!" },
                    ]}
                >
                    <TextArea rows={4} placeholder="Введіть текст..." maxLength={200} allowClear />
                </Form.Item>

                <Form.Item label="Фото продукту" name="imageFiles">
                    <input
                        type="file"
                        multiple
                        accept="image/*"
                        onChange={handleFileChange}
                        className="w-full p-2 border border-gray-300 rounded mt-2"
                    />
                </Form.Item>

                {/* Відображення вибраних зображень з можливістю перетягувати */}
                {selectedFiles.length > 0 && (
                    // @ts-ignore
                    <SortableList files={selectedFiles} distance={1} onSortEnd={onSortEnd} axis="xy" />
                )}

                <div className="flex justify-center">
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="bg-blue-500 text-white p-2 rounded w-full md:w-1/2 mt-4"
                    >
                        {isLoading ? 'Updating...' : 'Update Product'}
                    </button>
                </div>

                {error && <p className="text-red-500 mt-2">Error updating product!</p>}
            </Form>
        </div>
    );
};

export default EditProductPage;
