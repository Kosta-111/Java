import React, { useState } from 'react';
import { Button, Modal, Table } from 'flowbite-react';
import { HiOutlineExclamationCircle } from "react-icons/hi";
import { LiaEdit } from "react-icons/lia";
import { FaRegTrashAlt } from "react-icons/fa";
import { useDeleteProductMutation, useGetAllProductsQuery } from "../../services/productsApi.ts";
import { APP_ENV } from "../../env";
import { Link } from "react-router-dom";

const ProductsPage: React.FC = () => {
    const { data: products, error, isLoading } = useGetAllProductsQuery();
    const [deleteProduct, { isLoading: isDeleting }] = useDeleteProductMutation();
    const [productToDelete, setProductToDelete] = useState<number | null>(null);
    const [openModal, setOpenModal] = useState<boolean>(false);

    // Обробник відкриття модального вікна
    const openDeleteModal = (id: number) => {
        setProductToDelete(id);
        setOpenModal(true);
    };

    // Обробник підтвердження видалення
    const handleDelete = async () => {
        if (productToDelete) {
            try {
                await deleteProduct(productToDelete).unwrap();
            } catch (err) {
                console.error('Error deleting product:', err);
            }
        }
        closeDeleteModal();
    };

    // Обробник закриття модального вікна
    const closeDeleteModal = () => {
        setOpenModal(false);        // Закриваємо модальне вікно після видалення
        setProductToDelete(null);  // Скидаємо продукт
    };

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error occurred while fetching products.</p>;

    return (
        <>
            <h1 className="text-4xl text-center font-bold text-blue-700 p-6 ">
                Products
            </h1>
            {/* Кнопка для переходу на сторінку створення продукту */}
            <div className="flex justify-start mb-6">
                <Link to="create" // Вказуємо маршрут для створення продукту
                      className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700"
                >
                    Create product
                </Link>
            </div>

            <div className="overflow-x-auto">
                <Table className="table-fixed w-full">
                    <Table.Head>
                        <Table.HeadCell className="w-12">Id</Table.HeadCell>
                        <Table.HeadCell className="w-32">Name</Table.HeadCell>
                        <Table.HeadCell className="w-32">Image</Table.HeadCell>
                        <Table.HeadCell className="w-24">Category</Table.HeadCell>
                        <Table.HeadCell className="w-64">Description</Table.HeadCell>
                        <Table.HeadCell className="w-16">Price</Table.HeadCell>
                        <Table.HeadCell className="w-16">Amount</Table.HeadCell>
                        <Table.HeadCell className="w-24">Actions</Table.HeadCell>
                    </Table.Head>
                    <Table.Body className="divide-y">
                        {products?.map((product) => (
                            <Table.Row key={product.id} className="bg-white dark:border-gray-700 dark:bg-gray-800">
                                <Table.Cell>
                                    {product.id}
                                </Table.Cell>
                                <Table.Cell className="font-medium text-gray-900 dark:text-white">
                                    {product.name}
                                </Table.Cell>
                                <Table.Cell>
                                    {product.images.map((image) => (
                                        <img
                                            src={(APP_ENV.REMOTE_IMAGES_URL + 'medium/' + image)}
                                            alt={product.name}
                                            style={{maxHeight: "75px", maxWidth: "75px", float: "left", margin: "3px"}}
                                        />
                                    ))}
                                </Table.Cell>
                                <Table.Cell>
                                    {product.categoryName}
                                </Table.Cell>
                                <Table.Cell>
                                    {product.description}
                                </Table.Cell>
                                <Table.Cell>
                                    {product.price}$
                                </Table.Cell>
                                <Table.Cell>
                                    {product.amount}
                                </Table.Cell>
                                <Table.Cell>
                                    <div className="flex">
                                        <Link to={`edit/${product.id}`}>
                                            <LiaEdit className="mx-1 h-6 w-6 text-gray-700" />
                                        </Link>
                                        <a href='#'>
                                            <FaRegTrashAlt onClick={() => openDeleteModal(product.id)} className="mx-1 h-6 w-6 text-red-800" />
                                        </a>
                                    </div>
                                </Table.Cell>
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table>
            </div>

            <Modal dismissible show={openModal} size="md" onClose={() => closeDeleteModal()} popup>
                <Modal.Header />
                <Modal.Body>
                    <div className="text-center">
                        <HiOutlineExclamationCircle className="mx-auto mb-4 h-14 w-14 text-gray-400 dark:text-gray-200" />
                        <h3 className="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
                            Are you sure you want to delete this product?
                        </h3>
                        <div className="flex justify-center gap-4">
                            <Button color="failure" onClick={() => handleDelete()} disabled={isDeleting}>
                                {isDeleting ? "Deleting..." : "Yes, I'm sure"}
                            </Button>
                            <Button color="gray" onClick={() => closeDeleteModal()}>
                                No, cancel
                            </Button>
                        </div>
                    </div>
                </Modal.Body>
            </Modal>
        </>
    );
};

export default ProductsPage;