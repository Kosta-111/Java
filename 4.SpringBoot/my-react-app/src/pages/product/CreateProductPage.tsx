import React, { useState } from 'react';
import { useCreateProductMutation } from "../../services/productsApi.ts";
import { useNavigate } from 'react-router-dom';
import { IProductCreate } from "../../types/Product.ts";
import { useGetAllCategoriesQuery } from "../../services/categoriesApi.ts";
import { Form, Input, Select } from "antd";
import TextArea from "antd/es/input/TextArea";
import { CloseCircleOutlined } from '@ant-design/icons';
import { SortableContainer, SortableElement } from 'react-sortable-hoc';

const CreateProductPage: React.FC = () => {
    const { data: categories, isLoading: categoriesLoading, error: categoriesError } = useGetAllCategoriesQuery();
    const [createProduct, { isLoading, error }] = useCreateProductMutation();
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const navigate = useNavigate();
    const [form] = Form.useForm<IProductCreate>();

    const categoriesData = categories?.map(item => ({
        label: item.name,
        value: item.id,
    }));

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            const filesArray = Array.from(event.target.files);
            setSelectedFiles(prev => [...prev, ...filesArray]);
        }
    };

    const handleRemoveFile = (index: number) => {
        setSelectedFiles(prev => prev.filter((_, i) => i !== index)); // Видалення файлів
    };

    const onSubmit = async (values: IProductCreate) => {
        try {
            values.imageFiles = selectedFiles;
            await createProduct(values).unwrap();
            navigate('..');
        } catch (err) {
            console.error('Error creating product:', err);
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
            {/* Кнопка видалення */}
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

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-2xl font-bold text-center mb-6">Create Product</h1>
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
                        rules={[{ required: true, message: "It is a required field!" }]}
                    >
                        <Select placeholder="Оберіть категорію" options={categoriesData} />
                    </Form.Item>
                )}

                <Form.Item
                    label="Ціна"
                    name="price"
                    htmlFor="price"
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
                        {isLoading ? 'Creating...' : 'Create Product'}
                    </button>
                </div>

                {error && <p className="text-red-500 mt-2">Error creating product!</p>}
            </Form>
        </div>
    );
};

export default CreateProductPage;

