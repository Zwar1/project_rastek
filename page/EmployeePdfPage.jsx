import React, { useState, useEffect } from "react";
import MasterLayout from "../masterLayout/MasterLayout";
import Breadcrumb from "../components/NavigationTitle";
import EmployeePdf from "../components/EmployeePdf";
import { useSelector } from "react-redux";
import useGetEmployee from "../hook/useGetEmployee";
import useGetCV from "../hook/useGetCV";

const EmployeePdfPage = () => {
  const EmployeeList = useGetEmployee();
  //const EmployeeCVList = useGetCV();
  const [employeeData, setEmployeeData] = useState(null);
  const dumpnik = useSelector((state) => state.employee.dumpnik);

  useEffect(() => {
    console.log("Dumpnik:", dumpnik); // Log dumpnik value
    console.log("Employee List:", EmployeeList); // Log employee list

    if (EmployeeList.length > 0 && dumpnik) {
      // Find the employee with the matching dumpnik
      const foundEmployee = EmployeeList.find(
        (employee) => employee.nik === dumpnik
      );
      console.log("Found Employee:", foundEmployee); // Log found employee
      setEmployeeData(foundEmployee || null); // Set employee data or null if not found
    }
  }, [EmployeeList, dumpnik]);

  const handlePrint = () => {
    document.title = `CV_${employeeData.name}`;
    window.print();
  };

  return (
    <>
      <MasterLayout>
        <Breadcrumb />
        <div id="pdf-content">
          <EmployeePdf />
        </div>
        <div className="d-flex justify-content-center py-3">
          <button className="btn btn-primary px-5" onClick={handlePrint}>
            Download
          </button>
        </div>
      </MasterLayout>
    </>
  );
};

export default EmployeePdfPage;