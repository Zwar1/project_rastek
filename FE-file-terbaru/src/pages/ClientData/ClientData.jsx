import { Icon } from "@iconify/react/dist/iconify.js";
import React from "react";
import { Link } from "react-router-dom";
import useGetClient from "../../hook/useGetClient";
import { useNavigate } from "react-router-dom";
import { useDispatch } from 'react-redux';
import { setSelectedClientId } from '../../ReduxStore/clientReducer';

const ClientData = () => {
  const clientList = useGetClient();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleOpenClick = (client) => {
    console.log("Opening client:", client);
    dispatch(setSelectedClientId(client.id));
    console.log("Dispatched client ID:", client.id);
    navigate('/client-detail');
  };

  return (
    <div className="card h-100 p-0 radius-12">
      <div className="card-header border-bottom bg-base py-16 px-24 d-flex align-items-center flex-wrap gap-3 justify-content-between">
        <div className="d-flex align-items-center flex-wrap gap-3">
          <form className="navbar-search">
            <input
              type="text"
              className="bg-base h-40-px w-auto"
              name="search"
              placeholder="Search"
            />
            <Icon icon="ion:search-outline" className="icon" />
          </form>
          <div className="dropdown">
            <button
              className="btn btn-outline-primary-600 btn-sm radius-8 not-active px-16 py-9 dropdown-toggle toggle-icon"
              type="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {" "}
              Filter{" "}
            </button>
            <ul className="dropdown-menu">
              <li>
                <Link
                  className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                  to="#"
                >
                  All
                </Link>
              </li>
              <li>
                <Link
                  className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                  to="#"
                >
                  Active
                </Link>
              </li>
              <li>
                <Link
                  className="dropdown-item px-16 py-8 rounded text-secondary-light bg-hover-neutral-200 text-hover-neutral-900"
                  to="#"
                >
                  Inactive
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div className="card-body p-24">
        <div className="table-responsive scroll-sm">
          <table className="table bordered-table sm-table mb-0">
            <thead>
              <tr>
                <th scope="col">
                  <div className="d-flex align-items-center gap-10">No</div>
                </th>
                {/* <th scope="col" className="text-center">NIP</th> */}
                <th scope="col">Name</th>
                <th scope="col">Contact Number</th>
                <th scope="col">Email</th>
                <th scope="col">Country</th>
                <th scope="col" className="text-center">
                  Status
                </th>
                <th scope="col" className="text-center">
                  Action
                </th>
              </tr>
            </thead>
            <tbody>
              {clientList.map((client, index) => (
                <tr key={index}>
                  <td>
                    <div className="d-flex align-items-center gap-10">
                      {index + 1}
                    </div>
                  </td>
                  <td>
                    <div className="d-flex align-items-center">
                      <div className="flex-grow-1">
                        <span className="text-md mb-0 fw-normal text-secondary-light">
                          {client.clientName}
                        </span>
                      </div>
                    </div>
                  </td>
                  <td>
                    <span className="text-md mb-0 fw-normal text-secondary-light">
                      {client.clientContact}
                    </span>
                  </td>
                  <td>{client.clientEmail}</td>
                  <td>{client.clientCountry ? client.clientCountry : "-"}</td>
                  <td className="text-center">
                    <span
                      className={`bg-${client.isActive ? "success" : "danger"
                        }-focus text-${client.isActive ? "success" : "danger"
                        }-main px-20 py-6 rounded-pill fw-medium text-sm`}
                    >
                      {client.isActive ? "Active" : "Inactive"}
                    </span>
                  </td>
                  <td className="text-center">
                    <div className="d-flex align-items-center gap-10 justify-content-center">
                      <button
                        className="bg-info-focus bg-hover-info-200 text-info-600 fw-medium w-40-px h-40-px d-flex justify-content-center align-items-center rounded-circle"
                        onClick={() => handleOpenClick(client)}
                      >
                        <Icon
                          icon="majesticons:eye-line"
                          className="icon text-xl"
                        />
                      </button>
                      <button
                        type="button"
                        className="bg-success-focus text-success-600 bg-hover-success-200 fw-medium w-40-px h-40-px d-flex justify-content-center align-items-center rounded-circle"
                      >
                        <Icon icon="lucide:edit" className="menu-icon" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="d-flex align-items-center justify-content-between flex-wrap gap-2 mt-24">
          <span>Showing 1 to 10 of 75 entries</span>
          <ul className="pagination d-flex flex-wrap align-items-center gap-2 justify-content-center">
            <li className="page-item">
              <Link
                className="page-link bg-neutral-200 text-secondary-light fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px  text-md"
                to="#"
              >
                <Icon icon="ep:d-arrow-left" className="" />
              </Link>
            </li>
            <li className="page-item">
              <Link
                className="page-link text-secondary-light fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px w-32-px text-md bg-primary-600 text-white"
                to="#"
              >
                1
              </Link>
            </li>
            <li className="page-item">
              <Link
                className="page-link bg-neutral-200 text-secondary-light fw-semibold radius-8 border-0 d-flex align-items-center justify-content-center h-32-px  text-md"
                to="#"
              >
                {" "}
                <Icon icon="ep:d-arrow-right" className="" />{" "}
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ClientData;