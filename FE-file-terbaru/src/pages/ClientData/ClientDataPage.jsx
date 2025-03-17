import React from "react";
import MasterLayout from "../../masterLayout/MasterLayout";
import Breadcrumb from "../../components/NavigationTitle";
import ClientData from "./ClientData";

const ClientDataPage = () => {
  return (
    <>

      {/* MasterLayout */}
      <MasterLayout>

        {/* Breadcrumb */}
        <Breadcrumb title="Client Data" />

        <ClientData />  

      </MasterLayout>

    </>
  );
};

export default ClientDataPage; 