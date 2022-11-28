import React, { PureComponent } from "react";
import {
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";

export default class ChartFranchiseeCount extends PureComponent {
  static demoUrl = "https://codesandbox.io/s/simple-bar-chart-tpz8r";
  render() {
    const { info } = this.props;
    const franchiseeData = [
      {
        name: "2018",
        "점포 수": info?.franchiseeCount.at(-5),
        // pv: 2400,
        amt: 2400
      },
      {
        name: "2019",
        "점포 수": info?.franchiseeCount.at(-4),
        // pv: 1398,
        amt: 2210
      },
      {
        name: "2020",
        "점포 수": info?.franchiseeCount.at(-3),
        // pv: 9800,
        amt: 2290
      },
      {
        name: "2021",
        "점포 수": info?.franchiseeCount.at(-2),
        // pv: 3908,
        amt: 2000
      },
      {
        name: "2022",
        "점포 수": info?.franchiseeCount.at(-1),
        // pv: 4800,
        amt: 2181
      }
    ];
    console.log(info);
    return (
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          width={500}
          height={300}
          data={franchiseeData}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis domain={[0, 3000]} />
          <Tooltip />
          <Legend />
          {/* <Bar dataKey="pv" fill="#8884d8" /> */}
          <Bar dataKey="점포 수" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    );
  }
}
