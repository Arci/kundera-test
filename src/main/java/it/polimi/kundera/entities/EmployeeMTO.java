/**
 * Copyright 2013 deib-polimi
 * Contact: deib-polimi <marco.miglierina@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.kundera.entities;

import it.polimi.kundera.generate.RandomUtils;
import it.polimi.kundera.generate.Randomizable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "EmployeeMTO", schema = "gae@pu")
public class EmployeeMTO implements Randomizable<EmployeeMTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMPLOYEE_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SALARY")
    private Long salary;

    /* many employees work in one department */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Override
    public EmployeeMTO randomize() {
        setName(RandomUtils.randomString());
        setSalary(RandomUtils.randomLong());
        setDepartment(new Department().randomize());
        return this;
    }
}
