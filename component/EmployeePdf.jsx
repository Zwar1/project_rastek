import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import useGetEmployee from "../hook/useGetEmployee";

const EmployeePdf = () => {
  const [employeeData, setEmployeeData] = useState(null); // Initialize as null
  const EmployeeList = useGetEmployee();
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
  }, [EmployeeList, dumpnik]); // Run effect when EmployeeList or dumpnik changes

  if (!employeeData) {
    return <div className="text-center mt-5">Loading employee data...</div>;
  }

  return (
    // card cv
    <div id="print-content" className="container mt-2 p-40 rounde bg-white">
      <div className="mb-4">
        <h1 className="fw-bold mb-3">{employeeData.name}</h1>
        <p className="text-muted text-2xl">
          {employeeData.riwayatJabatan[0]?.kodeJabatan.departement}
        </p>
      </div>

      <div className="mb-4">
        <h5 className="text-primary pb-2">
          {employeeData.riwayatJabatan[0]?.kodeJabatan.namaJabatan} - Rastek.id
        </h5>
        <p className="text-muted text-lg">{employeeData.alamatDomisili}</p>
        <hr className="mt-5" />
      </div>

      {/* Body */}
      <div className="container my-5">
        <h4>Project</h4>
        <div className="row d-flex justify-content-between my-3 text-xl">
          {employeeData.cv.map((project, index) => (
            <div key={index} className="col-12 col-md-6 mb-4">
              <div className="project-card p-3 border rounded">
                <div>
                  <strong>{project.projectName} / </strong> {project.projectRole}
                  <div className="text-lg text-muted">
                    {new Date(project.projectStart).toLocaleDateString()} - {new Date(project.projectEnd).toLocaleDateString()}
                  </div>
                </div>
                <div>{project.projectDescription}</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* <div className="text-center mt-10">
        <p className="text-muted">
          Generated on: {new Date().toLocaleDateString()}
        </p>
      </div> */}
    </div>
  );
};

export default EmployeePdf;
